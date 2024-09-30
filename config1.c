#include <stdio.h>

// Função para mostrar informações de configuração
void show() {
    // Simulação de informações de configuração (exemplo simples)
    const char *version = "1.0";
    const char *platform = "C";
    const char *author = "C Dev Team";

    // Exibir informações de configuração
    printf("Configuração C:\nVersão: %s\nPlataforma: %s\nAutor: %s\n", version, platform, author);
}

// Função para retornar informações de flags de compilação (simulação)
void cxxFlags() {
    // Simulação de flags de compilação (exemplo simples)
    const char *flags = "-O2 -std=c11";

    printf("Flags de Compilação: %s\n", flags);
}

// Função para retornar informações de paralelização (simulação)
void parallelInfo() {
    // Simulação de informações de paralelização (exemplo simples)
    int threads = 4;
    const char *method = "OpenMP";

    printf("Informações de Paralelização:\nThreads: %d\nMétodo: %s\n", threads, method);
}

// Função principal para testar as funções
int main() {
    show();
    cxxFlags();
    parallelInfo();
    return 0;
}
