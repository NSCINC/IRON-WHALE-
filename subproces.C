#include <stdio.h>
#include <string.h>

// Função de validação do token NSC
int validate_nsc_token(const char *token) {
    return strcmp(token, "valid_token") == 0; // Exemplo simplificado
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Uso: ./nsc_token_validator <token>\n");
        return 1;
    }

    const char *token = argv[1];
    if (validate_nsc_token(token)) {
        printf("valid\n");
    } else {
        printf("invalid\n");
    }

    return 0;
}
