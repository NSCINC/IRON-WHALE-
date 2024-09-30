import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

// Definição da tabela users
object Users : IntIdTable() {
    val name = varchar("name", 100)
    val email = varchar("email", 100)
    val token = varchar("token", 255)
}

// Definição da tabela crm_data
object CrmData : IntIdTable() {
    val userId = reference("user_id", Users)
    val data = jsonb("data") // Usando jsonb para armazenar dados JSON
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

// Definição da tabela erp_data
object ErpData : IntIdTable() {
    val userId = reference("user_id", Users)
    val data = jsonb("data") // Usando jsonb para armazenar dados JSON
    val createdAt = datetime("created_at").default(LocalDateTime.now())
}

// Função para conectar ao banco de dados e criar as tabelas
fun initDatabase() {
    Database.connect("jdbc:postgresql://localhost:5432/your_database", driver = "org.postgresql.Driver",
        user = "your_user", password = "your_password")

    transaction {
        SchemaUtils.create(Users, CrmData, ErpData) // Cria as tabelas no banco de dados
    }
}

fun main() {
    initDatabase() // Chama a função para inicializar o banco de dados
}
