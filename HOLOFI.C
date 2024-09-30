#include <stdio.h>
#include <stdlib.h>
#include <fann.h>

// Configurações da rede neural
const unsigned int NUM_INPUT = 10;       // Número de entradas
const unsigned int NUM_HIDDEN = 20;      // Número de neurônios na camada oculta
const unsigned int NUM_OUTPUT = 1;       // Número de saídas
const unsigned int MAX_EPOCHS = 1000;    // Número máximo de épocas de treinamento
const float ERROR_THRESHOLD = 0.01;       // Limite de erro para o treinamento

// Função para criar e treinar a rede neural
void create_and_train_neural_network() {
    // Criando a rede neural com 3 camadas: entrada, oculta e saída
    struct fann *ann = fann_create_standard(3, NUM_INPUT, NUM_HIDDEN, NUM_OUTPUT);
    if (ann == NULL) {
        fprintf(stderr, "Error creating neural network\n");
        return;
    }

    // Configurando as funções de ativação
    fann_set_activation_function_hidden(ann, FANN_SIGMOID);
    fann_set_activation_function_output(ann, FANN_SIGMOID);

    // Definindo os dados de treinamento
    struct fann_train_data *train_data = fann_create_train(2, NUM_INPUT, NUM_OUTPUT);
    if (train_data == NULL) {
        fprintf(stderr, "Error creating training data\n");
        fann_destroy(ann);
        return;
    }

    // Exemplo de dados fictícios para treinamento
    fann_set_input(train_data, 0, (float[]){1, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    fann_set_output(train_data, 0, (float[]){1});
    fann_set_input(train_data, 1, (float[]){0, 1, 0, 0, 0, 0, 0, 0, 0, 0});
    fann_set_output(train_data, 1, (float[]){1});

    // Treinando a rede neural
    fann_train_on_data(ann, train_data, MAX_EPOCHS, 10, ERROR_THRESHOLD);
    
    // Salvando a rede neural treinada em um arquivo
    fann_save(ann, "neural_network.net");

    // Liberando memória dos dados de treinamento e da rede neural
    fann_destroy_train(train_data);
    fann_destroy(ann);
}

// Função para carregar a rede neural e realizar previsões
void predict_with_neural_network() {
    // Carregando a rede neural treinada
    struct fann *ann = fann_create_from_file("neural_network.net");
    if (ann == NULL) {
        fprintf(stderr, "Error loading neural network\n");
        return;
    }

    // Dados de entrada para previsão
    float input[NUM_INPUT] = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    float *output;

    // Realizando a previsão
    output = fann_run(ann, input);
    printf("Prediction: %f\n", output[0]);

    // Liberando a memória da rede neural
    fann_destroy(ann);
}

int main() {
    // Criar e treinar a rede neural
    create_and_train_neural_network();

    // Realizar previsões com a rede neural treinada
    predict_with_neural_network();

    return 0;
}
