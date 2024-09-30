#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <unistd.h>
#include <curl/curl.h>
#include <json-c/json.h>

#define MAX_WEIGHTS 3

// Estrutura para o pedido de investimento
typedef struct {
    char token[100];
    char plan_name[100];
    int initial_investment;
    int amount;
    char investor_address[100];
} InvestmentRequest;

// Validação do token NSC (exemplo simples)
int validate_nsc_token(const char *token) {
    return strcmp(token, "valid_token") == 0;
}

// Função para executar o script Lua
int run_lua_contract(const char *script, const char *function, char *args[], int arg_count, char *output, size_t output_size) {
    char command[256];
    snprintf(command, sizeof(command), "lua %s %s", script, function);
    for (int i = 0; i < arg_count; i++) {
        strcat(command, " ");
        strcat(command, args[i]);
    }
    
    FILE *fp = popen(command, "r");
    if (fp == NULL) {
        perror("popen");
        return -1;
    }
    
    fread(output, sizeof(char), output_size, fp);
    pclose(fp);
    return 0;
}

// Função para adicionar um plano
void add_plan(InvestmentRequest *request) {
    if (!validate_nsc_token(request->token)) {
        printf("Token NSC inválido\n");
        return;
    }

    char output[256];
    char *args[] = {request->plan_name, itoa(request->initial_investment, (char[12]){0}, 10)};
    
    if (run_lua_contract("investment_contract.lua", "add_plan", args, 2, output, sizeof(output)) == 0) {
        printf("Resultado: %s\n", output);
    } else {
        printf("Error executing Lua contract\n");
    }
}

// Função para investir em um plano
void invest(InvestmentRequest *request) {
    char output[256];
    char *args[] = {request->plan_name, itoa(request->amount, (char[12]){0}, 10), request->investor_address};
    
    if (run_lua_contract("investment_contract.lua", "invest", args, 3, output, sizeof(output)) == 0) {
        printf("Resultado: %s\n", output);
    } else {
        printf("Error executing Lua contract\n");
    }
}

// Rede Neural Simples
typedef struct {
    double weights[MAX_WEIGHTS];
} SimpleNeuralNetwork;

// Inicializa a rede neural com pesos aleatórios
SimpleNeuralNetwork* new_simple_neural_network() {
    SimpleNeuralNetwork *nn = malloc(sizeof(SimpleNeuralNetwork));
    for (int i = 0; i < MAX_WEIGHTS; i++) {
        nn->weights[i] = (double)rand() / RAND_MAX;
    }
    return nn;
}

// Função de ativação Sigmoid
double sigmoid(double x) {
    return 1.0 / (1.0 + exp(-x));
}

// Calcula a saída da rede neural
double feedforward(SimpleNeuralNetwork *nn, double *input_data) {
    double weighted_sum = 0.0;
    for (int i = 0; i < MAX_WEIGHTS; i++) {
        weighted_sum += input_data[i] * nn->weights[i];
    }
    return sigmoid(weighted_sum);
}

// Processa dados de CRM
double process_crm_data(SimpleNeuralNetwork *nn, double *crm_data) {
    return feedforward(nn, crm_data);
}

// Função principal
int main() {
    // Inicializa a rede neural
    SimpleNeuralNetwork *neural_network = new_simple_neural_network();
    
    double crm_data[MAX_WEIGHTS] = {500.0, 100.0, 5.0};
    double result = process_crm_data(neural_network, crm_data);
    printf("Resultado do processamento de CRM: %.2f\n", result);
    
    // Simulando um pedido de investimento
    InvestmentRequest request = {
        .token = "valid_token",
        .plan_name = "Investment Plan A",
        .initial_investment = 1000,
        .amount = 500,
        .investor_address = "0x123456789"
    };
    add_plan(&request);
    invest(&request);

    free(neural_network);
    return 0;
}
