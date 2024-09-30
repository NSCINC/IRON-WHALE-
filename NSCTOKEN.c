#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sentencepiece/sentencepiece_processor.h>

typedef struct {
    sp::SentencePieceProcessor *spModel;
    int nWords;
    int bosId;
    int eosId;
    int padId;
} Tokenizer;

// Inicializa o Tokenizer
Tokenizer *init_tokenizer(const char *model_path) {
    Tokenizer *tokenizer = (Tokenizer *)malloc(sizeof(Tokenizer));
    tokenizer->spModel = new sp::SentencePieceProcessor();
    
    // Carregar o modelo
    if (!tokenizer->spModel->Load(model_path).ok()) {
        fprintf(stderr, "Model file not found: %s\n", model_path);
        free(tokenizer);
        return NULL;
    }
    
    tokenizer->nWords = tokenizer->spModel->GetPieceSize();
    tokenizer->bosId = tokenizer->spModel->bos_id();
    tokenizer->eosId = tokenizer->spModel->eos_id();
    tokenizer->padId = tokenizer->spModel->pad_id();

    printf("#words: %d - BOS ID: %d - EOS ID: %d\n", tokenizer->nWords, tokenizer->bosId, tokenizer->eosId);
    
    return tokenizer;
}

// Codifica uma string em uma lista de IDs de token
int *encode(Tokenizer *tokenizer, const char *text, int *out_size, int add_bos, int add_eos) {
    std::vector<int> ids;
    tokenizer->spModel->Encode(text, &ids);
    
    // Calcular o tamanho da saída
    int size = ids.size();
    if (add_bos) {
        size += 1; // espaço para BOS
    }
    if (add_eos) {
        size += 1; // espaço para EOS
    }

    // Alocar memória para os IDs de saída
    int *tokenIds = (int *)malloc(size * sizeof(int));
    int index = 0;

    // Adicionar BOS ID, se necessário
    if (add_bos) {
        tokenIds[index++] = tokenizer->bosId;
    }

    // Adicionar IDs de tokens
    for (int id : ids) {
        tokenIds[index++] = id;
    }

    // Adicionar EOS ID, se necessário
    if (add_eos) {
        tokenIds[index++] = tokenizer->eosId;
    }

    *out_size = size; // Retornar o tamanho do array de saída
    return tokenIds;
}

// Decodifica uma lista de IDs de token em uma string
char *decode(Tokenizer *tokenizer, int *tokenIds, int size) {
    std::vector<int> ids(tokenIds, tokenIds + size);
    std::string decoded = tokenizer->spModel->Decode(ids);
    
    // Alocar memória para a string de saída
    char *result = (char *)malloc((decoded.length() + 1) * sizeof(char));
    strcpy(result, decoded.c_str());
    return result;
}

// Libera a memória do Tokenizer
void free_tokenizer(Tokenizer *tokenizer) {
    delete tokenizer->spModel;
    free(tokenizer);
}

int main() {
    const char *model_path = "path/to/your/sentencepiece/model.model";
    Tokenizer *tokenizer = init_tokenizer(model_path);
    
    if (!tokenizer) {
        return EXIT_FAILURE; // Saia se o tokenizer não foi inicializado
    }

    // Exemplo de uso
    const char *text = "Hello, world!";
    int size;
    int *encoded = encode(tokenizer, text, &size, 1, 1); // Adiciona BOS e EOS

    printf("Encoded: ");
    for (int i = 0; i < size; i++) {
        printf("%d ", encoded[i]);
    }
    printf("\n");

    char *decoded = decode(tokenizer, encoded, size);
    printf("Decoded: %s\n", decoded);

    // Libera memória
    free(encoded);
    free(decoded);
    free_tokenizer(tokenizer);

    return EXIT_SUCCESS;
}
