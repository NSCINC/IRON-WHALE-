import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    // Inicia o servidor na porta 8080
    embeddedServer(Netty, port = 8080) {
        routing {
            // Configura os endpoints para o servidor
            loginEndpoint()
            logoutEndpoint()
            blocksEndpoint()
        }
    }.start(wait = true)
}

// Função para configurar o endpoint de login
fun Route.loginEndpoint() {
    get("/login") {
        call.respondText("Endpoint de Login - Brain.kt", ContentType.Text.Plain)
    }
}

// Função para configurar o endpoint de logout
fun Route.logoutEndpoint() {
    get("/logout") {
        call.respondText("Endpoint de Logout - Brain.kt", ContentType.Text.Plain)
    }
}

// Função para configurar o endpoint de blocos
fun Route.blocksEndpoint() {
    get("/blocks") {
        call.respondText("Endpoint de Blocos - Brain.kt", ContentType.Text.Plain)
    }
}
dependencies {
    implementation("io.ktor:ktor-server-netty:2.x.x") // Substitua 2.x.x pela versão mais recente
    implementation("io.ktor:ktor-html-builder:2.x.x")
    implementation("io.ktor:ktor-server-core:2.x.x")
}
