#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include <openssl/sha.h>  // For SHA256
#include <unistd.h>       // For sleep

#define MAX_SIZE 3
#define TOKEN_EXPIRATION 300 // 5 minutes in seconds

// Token structure
typedef struct {
    char token_id[65]; // SHA256 produces a 64-character hex string + null terminator
    time_t expiration_time;
} NSC_IAToken;

// SEA Asset structure
typedef struct {
    char origin[50];
    char created_at[30];
    double value;
} SEAAsset;

// Cache structure
typedef struct {
    char keys[MAX_SIZE][50];
    SEAAsset values[MAX_SIZE];
    int current_size;
    pthread_mutex_t lock;
} Cache;

// Function to generate NSC IA Token
NSC_IAToken generate_nsc_ia_token() {
    NSC_IAToken token;
    snprintf(token.token_id, sizeof(token.token_id), "%f", (double)time(NULL));
    unsigned char hash[SHA256_DIGEST_LENGTH];
    SHA256((unsigned char*)token.token_id, strlen(token.token_id), hash);
    for (int i = 0; i < SHA256_DIGEST_LENGTH; i++) {
        sprintf(&token.token_id[i * 2], "%02x", hash[i]);
    }
    token.expiration_time = time(NULL) + TOKEN_EXPIRATION;
    return token;
}

// Function to check if token is valid
int is_token_valid(NSC_IAToken* token) {
    return time(NULL) < token->expiration_time;
}

// Function to initialize cache
void init_cache(Cache* cache) {
    cache->current_size = 0;
    pthread_mutex_init(&cache->lock, NULL);
}

// Function to add to cache
void add_to_cache(Cache* cache, NSC_IAToken* token, const char* key, double value, const char* origin) {
    if (!is_token_valid(token)) {
        printf("Token inválido. Operação negada.\n");
        return;
    }

    pthread_mutex_lock(&cache->lock);
    
    if (cache->current_size >= MAX_SIZE) {
        printf("Removido do cache (LRU): %s\n", cache->keys[0]);
        for (int i = 1; i < MAX_SIZE; i++) {
            strcpy(cache->keys[i - 1], cache->keys[i]);
            cache->values[i - 1] = cache->values[i];
        }
        cache->current_size--;
    }

    SEAAsset sea_asset;
    sea_asset.value = value;
    strncpy(sea_asset.origin, origin, sizeof(sea_asset.origin) - 1);
    snprintf(sea_asset.created_at, sizeof(sea_asset.created_at), "%ld", time(NULL));

    strcpy(cache->keys[cache->current_size], key);
    cache->values[cache->current_size] = sea_asset;
    cache->current_size++;
    printf("Novo item adicionado ao cache: %s\n", key);
    
    pthread_mutex_unlock(&cache->lock);
}

// Function to get from cache
double get_from_cache(Cache* cache, NSC_IAToken* token, const char* key) {
    if (!is_token_valid(token)) {
        printf("Token inválido. Operação negada.\n");
        return -1;
    }

    pthread_mutex_lock(&cache->lock);

    for (int i = 0; i < cache->current_size; i++) {
        if (strcmp(cache->keys[i], key) == 0) {
            printf("Item encontrado no cache: %s\n", key);
            printf("Valor: %.2f\n", cache->values[i].value);
            pthread_mutex_unlock(&cache->lock);
            return cache->values[i].value;
        }
    }

    pthread_mutex_unlock(&cache->lock);
    printf("Item não encontrado no cache: %s\n", key);
    return -1;
}

// Function to remove from cache
void remove_from_cache(Cache* cache, NSC_IAToken* token, const char* key) {
    if (!is_token_valid(token)) {
        printf("Token inválido. Operação negada.\n");
        return;
    }

    pthread_mutex_lock(&cache->lock);

    for (int i = 0; i < cache->current_size; i++) {
        if (strcmp(cache->keys[i], key) == 0) {
            printf("Item removido do cache: %s\n", key);
            for (int j = i; j < cache->current_size - 1; j++) {
                strcpy(cache->keys[j], cache->keys[j + 1]);
                cache->values[j] = cache->values[j + 1];
            }
            cache->current_size--;
            break;
        }
    }

    pthread_mutex_unlock(&cache->lock);
}

// Function to list cache items
void list_cache(Cache* cache, NSC_IAToken* token) {
    if (!is_token_valid(token)) {
        printf("Token inválido. Operação negada.\n");
        return;
    }

    pthread_mutex_lock(&cache->lock);
    printf("Listando todos os itens no cache:\n");
    for (int i = 0; i < cache->current_size; i++) {
        printf("%s: %.2f (Origin: %s, Created At: %s)\n", cache->keys[i], cache->values[i].value, cache->values[i].origin, cache->values[i].created_at);
    }
    pthread_mutex_unlock(&cache->lock);
}

// Thread function for cache operations
void* cache_operations(void* arg) {
    Cache* cache = (Cache*)arg;
    NSC_IAToken token = generate_nsc_ia_token();

    add_to_cache(cache, &token, "Chave1", 100.0, "Sistema A");
    add_to_cache(cache, &token, "Chave2", 200.0, "Sistema B");
    get_from_cache(cache, &token, "Chave1");
    remove_from_cache(cache, &token, "Chave1");

    return NULL;
}

// Main function
int main() {
    NSC_IAToken token = generate_nsc_ia_token();
    Cache my_cache;
    init_cache(&my_cache);

    // Create two threads for cache operations
    pthread_t thread1, thread2;

    pthread_create(&thread1, NULL, cache_operations, &my_cache);
    pthread_create(&thread2, NULL, cache_operations, &my_cache);

    // Wait for threads to finish
    pthread_join(thread1, NULL);
    pthread_join(thread2, NULL);

    // List remaining items in the cache
    list_cache(&my_cache, &token);

    return 0;
}
