package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os/exec"
	"math/rand"
	"time"
)

// Estrutura para o pedido de investimento
type InvestmentRequest struct {
	Token             string `json:"token"`
	PlanName          string `json:"plan_name"`
	InitialInvestment int    `json:"initial_investment,omitempty"`
	Amount            int    `json:"amount,omitempty"`
	InvestorAddress   string `json:"investor_address,omitempty"`
}

// Validação do token NSC (exemplo simples)
func validateNscToken(token string) bool {
	return token == "valid_token"
}

// Função para executar o script Lua
func runLuaContract(script, function string, args ...string) (string, error) {
	cmd := exec.Command("lua", script, function)
	cmd.Args = append(cmd.Args, args...)
	output, err := cmd.CombinedOutput()
	return string(output), err
}

// Manipulador para adicionar um plano
func addPlan(w http.ResponseWriter, r *http.Request) {
	var request InvestmentRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, "Invalid request", http.StatusBadRequest)
		return
	}

	// Validar o token NSC
	if !validateNscToken(request.Token) {
		http.Error(w, "Token NSC inválido", http.StatusBadRequest)
		return
	}

	// Executa o contrato Lua
	result, err := runLuaContract("investment_contract.lua", "add_plan", request.PlanName, fmt.Sprint(request.InitialInvestment))
	if err != nil {
		http.Error(w, "Error executing Lua contract", http.StatusInternalServerError)
		return
	}

	w.Write([]byte(result))
}

// Manipulador para investir em um plano
func invest(w http.ResponseWriter, r *http.Request) {
	var request InvestmentRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, "Invalid request", http.StatusBadRequest)
		return
	}

	// Executa o contrato Lua
	result, err := runLuaContract("investment_contract.lua", "invest", request.PlanName, fmt.Sprint(request.Amount), request.InvestorAddress)
	if err != nil {
		http.Error(w, "Error executing Lua contract", http.StatusInternalServerError)
		return
	}

	w.Write([]byte(result))
}

// Rede Neural Simples
type SimpleNeuralNetwork struct {
	Weights []float64
}

// Inicializa a rede neural com pesos aleatórios
func NewSimpleNeuralNetwork(inputSize int) *SimpleNeuralNetwork {
	weights := make([]float64, inputSize)
	rand.Seed(time.Now().UnixNano())
	for i := range weights {
		weights[i] = rand.Float64()
	}
	return &SimpleNeuralNetwork{Weights: weights}
}

// Função de ativação Sigmoid
func (nn *SimpleNeuralNetwork) Sigmoid(x float64) float64 {
	return 1 / (1 + math.Exp(-x))
}

// Calcula a saída da rede neural
func (nn *SimpleNeuralNetwork) Feedforward(inputData []float64) float64 {
	var weightedSum float64
	for i := range inputData {
		weightedSum += inputData[i] * nn.Weights[i]
	}
	return nn.Sigmoid(weightedSum)
}

// Processa dados de CRM
func (nn *SimpleNeuralNetwork) ProcessCrmData(crmData []float64) float64 {
	return nn.Feedforward(crmData)
}

// Função principal
func main() {
	http.HandleFunc("/api/add-plan", addPlan)
	http.HandleFunc("/api/invest", invest)

	// Inicializa a rede neural
	neuralNetwork := NewSimpleNeuralNetwork(3) // Exemplo de entrada com 3 features
	crmData := []float64{500.0, 100.0, 5.0}
	result := neuralNetwork.ProcessCrmData(crmData)
	fmt.Printf("Resultado do processamento de CRM: %.2f\n", result)

	// Inicia o servidor na porta 8080
	fmt.Println("Servidor rodando na porta 8080...")
	http.ListenAndServe(":8080", nil)
}
