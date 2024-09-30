// SPDX-License-Identifier: MIT
// Copyright 2024 NSC INCS, Lucas Januário

// InvestmentContract
class InvestmentContract(private val owner: String) {
    private val balances: MutableMap<String, Int> = mutableMapOf()
    private val investedAmount: MutableMap<String, Int> = mutableMapOf()
    private val authorizedInvestors: MutableMap<String, Boolean> = mutableMapOf()

    // Permite que um investidor faça um investimento
    fun invest(amount: Int, sender: String) {
        require(authorizedInvestors[sender] == true) { "Investor is not authorized" }
        require(amount > 0) { "Investment amount must be greater than zero" }
        require(amount <= (balances[sender] ?: 0)) { "Insufficient balance" }

        balances[sender] = (balances[sender] ?: 0) - amount
        investedAmount[sender] = (investedAmount[sender] ?: 0) + amount

        emit("Investment", sender, amount)
    }

    // Autoriza ou desautoriza um investidor
    fun authorizeInvestor(investor: String, authorized: Boolean) {
        require(owner == investor) { "Only owner can perform this action" }
        authorizedInvestors[investor] = authorized
        emit("AuthorizationChanged", investor, authorized)
    }

    // Obtém o saldo de um investidor
    fun balanceOf(investor: String): Int {
        return balances[investor] ?: 0
    }

    // Obtém o valor investido por um investidor
    fun investedAmountOf(investor: String): Int {
        return investedAmount[investor] ?: 0
    }

    // Simula a emissão de eventos
    private fun emit(event: String, vararg args: Any) {
        println("Event $event emitted with args: ${args.joinToString(", ")}")
    }
}

// AuthenticationContract
class AuthenticationContract(private val owner: String) {
    private val messageAuthenticity: MutableMap<String, MutableMap<String, Boolean>> = mutableMapOf()

    // Autentica uma mensagem
    fun authenticateMessage(messageHash: String, sender: String) {
        if (messageAuthenticity[sender] == null) {
            messageAuthenticity[sender] = mutableMapOf()
        }
        require(messageAuthenticity[sender]!![messageHash] != true) { "Message already authenticated" }
        messageAuthenticity[sender]!![messageHash] = true
        emit("MessageAuthenticated", sender, messageHash, true)
    }

    // Verifica se uma mensagem foi autenticada
    fun isMessageAuthenticated(investor: String, messageHash: String): Boolean {
        return messageAuthenticity[investor]?.get(messageHash) == true
    }

    // Simula a emissão de eventos
    private fun emit(event: String, vararg args: Any) {
        println("Event $event emitted with args: ${args.joinToString(", ")}")
    }
}

// Exemplo de uso das funções definidas
fun main() {
    // Criar instâncias dos contratos
    val owner = "owner_address"
    val investor1 = "investor1_address"
    val investor2 = "investor2_address"

    val investmentContract = InvestmentContract(owner)
    val authenticationContract = AuthenticationContract(owner)

    // Adicionar saldo ao investidor
    investmentContract.authorizeInvestor(investor1, true)
    investmentContract.authorizeInvestor(investor2, true)
    investmentContract.balanceOf(investor1) // Initial balance is 0

    // Adicionar saldo manualmente para simulação
    investmentContract.balances[investor1] = 1000
    investmentContract.balances[investor2] = 1000

    // Investir e autenticar mensagens
    println("\nStep 1: Investment and Authentication")
    investmentContract.invest(500, investor1)
    authenticationContract.authenticateMessage("message_hash_1", investor1)

    // Consultar detalhes
    println("\nStep 2: Fetching Details")
    println("Investor1 Balance: ${investmentContract.balanceOf(investor1)}")
    println("Investor1 Invested Amount: ${investmentContract.investedAmountOf(investor1)}")
    println("Message Authenticated: ${authenticationContract.isMessageAuthenticated(investor1, "message_hash_1")}")

    // Fim dos exemplos
    println("\nKotlin code execution completed.")
}
