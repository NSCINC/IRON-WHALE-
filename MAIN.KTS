import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.exp
import kotlin.random.Random

// Estrutura para o pedido de investimento
@Serializable
data class InvestmentRequest(
    val token: String,
    val planName: String,
    val initialInvestment: Int? = null,
    val amount: Int? = null,
    val investorAddress: String? = null
)

// Validação do token NSC (exemplo simples)
fun validateNscToken(token: String): Boolean {
    return token == "valid_token"
}

// Função para executar o script Lua
fun runLuaContract(script: String, function: String, vararg args: String): String {
    val command = mutableListOf("lua", script, function).apply { addAll(args) }
    val process = ProcessBuilder(command).start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    return reader.readText()
}

// Manipulador para adicionar um plano
suspend fun addPlan(call: ApplicationCall) {
    val request = call.receive<InvestmentRequest>()

    // Validar o token NSC
    if (!validateNscToken(request.token)) {
        call.respond(HttpStatusCode.BadRequest, "Token NSC inválido")
        return
    }

    // Executa o contrato Lua
    val result = runLuaContract("investment_contract.lua", "add_plan", request.planName, request.initialInvestment.toString())
    call.respond(result)
}

// Manipulador para investir em um plano
suspend fun invest(call: ApplicationCall) {
    val request = call.receive<InvestmentRequest>()

    // Executa o contrato Lua
    val result = runLuaContract("investment_contract.lua", "invest", request.planName, request.amount.toString(), request.investorAddress ?: "")
    call.respond(result)
}

// Rede Neural Simples
class SimpleNeuralNetwork(inputSize: Int) {
    private val weights = DoubleArray(inputSize) { Random.nextDouble() }

    // Função de ativação Sigmoid
    private fun sigmoid(x: Double): Double {
        return 1 / (1 + exp(-x))
    }

    // Calcula a saída da rede neural
    fun feedforward(inputData: DoubleArray): Double {
        val weightedSum = inputData.zip(weights) { input, weight -> input * weight }.sum()
        return sigmoid(weightedSum)
    }

    // Processa dados de CRM
    fun processCrmData(crmData: DoubleArray): Double {
        return feedforward(crmData)
    }
}

// Função principal
fun main() {
    // Inicializa a rede neural
    val neuralNetwork = SimpleNeuralNetwork(3) // Exemplo de entrada com 3 features
    val crmData = doubleArrayOf(500.0, 100.0, 5.0)
    val result = neuralNetwork.processCrmData(crmData)
    println("Resultado do processamento de CRM: %.2f".format(result))

    // Inicia o servidor Ktor na porta 8080
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
        routing {
            post("/api/add-plan") {
                addPlan(call)
            }
            post("/api/invest") {
                invest(call)
            }
        }
    }.start(wait = true)
}
