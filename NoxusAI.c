#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/sha.h>

#define BUFFER_SIZE 213

// Estrutura para o NoxusAI
typedef struct {
    unsigned char buffer[BUFFER_SIZE];
    size_t buffer_size;
} NoxusAI;

// Inicializa a estrutura NoxusAI
void init_noxus_ai(NoxusAI *noxus) {
    memset(noxus->buffer, 0, BUFFER_SIZE);
    noxus->buffer_size = 0;
}

// Calcula o digest da mensagem
void digest(NoxusAI *noxus, unsigned char *output) {
    if (noxus->buffer_size != BUFFER_SIZE) {
        fprintf(stderr, "Buffer not filled with %d bytes\n", BUFFER_SIZE);
        exit(EXIT_FAILURE);
    }
    SHA256(noxus->buffer, BUFFER_SIZE, output);
}

// Atualiza o digest com um novo valor
void update(NoxusAI *noxus, unsigned char value) {
    if (noxus->buffer_size >= BUFFER_SIZE) {
        fprintf(stderr, "Buffer overflow\n");
        exit(EXIT_FAILURE);
    }
    noxus->buffer[noxus->buffer_size++] = value;
}

// Atualiza o digest com um array de bytes
void update_bytes(NoxusAI *noxus, unsigned char *bytes, size_t length) {
    for (size_t i = 0; i < length; i++) {
        update(noxus, bytes[i]);
    }
}

// Atualiza o digest com um inteiro em big-endian
void update_big_endian_int(NoxusAI *noxus, int value) {
    unsigned char byte_buffer[4];
    byte_buffer[0] = (value >> 24) & 0xFF;
    byte_buffer[1] = (value >> 16) & 0xFF;
    byte_buffer[2] = (value >> 8) & 0xFF;
    byte_buffer[3] = value & 0xFF;
    update_bytes(noxus, byte_buffer, sizeof(byte_buffer));
}

// Reseta o buffer do digest
void reset(NoxusAI *noxus) {
    noxus->buffer_size = 0;
}

int main() {
    // Testes
    NoxusAI noxusDigest;
    init_noxus_ai(&noxusDigest);

    // Atualizar o digest com 213 bytes
    for (int i = 0; i < BUFFER_SIZE; i++) {
        update(&noxusDigest, 0);
    }

    unsigned char digest_output[SHA256_DIGEST_LENGTH];
    digest(&noxusDigest, digest_output);

    // Imprime o digest esperado
    for (int i = 0; i < SHA256_DIGEST_LENGTH; i++) {
        printf("%02x", digest_output[i]);
    }
    printf("\n");

    // Mais testes aqui...

    return 0;
}
