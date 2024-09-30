import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

data class Investor(val name: String, val email: String, val phoneNumber: String)

data class Asset(val assetName: String, val assetType: String, val totalSlots: Int, val annualReturn: Double)

data class Investment(val investorId: Int, val assetId: Int, val investedAmount: Double)

object InvestmentManager {

    // Função para inicializar o banco de dados e criar tabelas
    @Throws(SQLException::class)
    fun initializeDatabase() {
        DriverManager.getConnection("jdbc:sqlite:investments.db").use { conn ->
            val stmt: Statement = conn.createStatement()

            // Criação das tabelas
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Investors (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "email TEXT NOT NULL, " +
                        "phone_number TEXT NOT NULL);"
            )

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Assets (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "asset_name TEXT NOT NULL, " +
                        "asset_type TEXT NOT NULL, " +
                        "total_slots INTEGER NOT NULL, " +
                        "annual_return REAL NOT NULL);"
            )

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Investments (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "investor_id INTEGER, " +
                        "asset_id INTEGER, " +
                        "invested_amount REAL, " +
                        "FOREIGN KEY(investor_id) REFERENCES Investors(id), " +
                        "FOREIGN KEY(asset_id) REFERENCES Assets(id));"
            )
        }
    }

    // Função para inserir investidores
    @Throws(SQLException::class)
    fun insertInvestor(investor: Investor) {
        DriverManager.getConnection("jdbc:sqlite:investments.db").use { conn ->
            val query = "INSERT INTO Investors (name, email, phone_number) VALUES (?, ?, ?)"
            val pstmt: PreparedStatement = conn.prepareStatement(query)
            pstmt.setString(1, investor.name)
            pstmt.setString(2, investor.email)
            pstmt.setString(3, investor.phoneNumber)
            pstmt.executeUpdate()
        }
    }

    // Função para inserir ativos
    @Throws(SQLException::class)
    fun insertAsset(asset: Asset) {
        DriverManager.getConnection("jdbc:sqlite:investments.db").use { conn ->
            val query = "INSERT INTO Assets (asset_name, asset_type, total_slots, annual_return) VALUES (?, ?, ?, ?)"
            val pstmt: PreparedStatement = conn.prepareStatement(query)
            pstmt.setString(1, asset.assetName)
            pstmt.setString(2, asset.assetType)
            pstmt.setInt(3, asset.totalSlots)
            pstmt.setDouble(4, asset.annualReturn)
            pstmt.executeUpdate()
        }
    }

    // Função para inserir investimentos
    @Throws(SQLException::class)
    fun insertInvestment(investment: Investment) {
        DriverManager.getConnection("jdbc:sqlite:investments.db").use { conn ->
            val query = "INSERT INTO Investments (investor_id, asset_id, invested_amount) VALUES (?, ?, ?)"
            val pstmt: PreparedStatement = conn.prepareStatement(query)
            pstmt.setInt(1, investment.investorId)
            pstmt.setInt(2, investment.assetId)
            pstmt.setDouble(3, investment.investedAmount)
            pstmt.executeUpdate()
        }
    }

    // Função para ler dados de Excel e inserir no banco de dados
    @Throws(IOException::class, SQLException::class)
    fun readExcelData(filePath: String) {
        FileInputStream(filePath).use { fis ->
            XSSFWorkbook(fis).use { workbook ->
                val sheet: Sheet = workbook.getSheetAt(0)

                for (row in sheet) {
                    if (row.rowNum == 0) continue // Pular a primeira linha (cabeçalho)

                    val name = row.getCell(0).stringCellValue
                    val email = row.getCell(1).stringCellValue
                    val phoneNumber = row.getCell(2).stringCellValue

                    val investor = Investor(name, email, phoneNumber)
                    insertInvestor(investor)
                }
            }
        }
    }
}

fun main() {
    try {
        InvestmentManager.initializeDatabase()
        InvestmentManager.readExcelData("investors_data.xlsx")  // Coloque o caminho do seu arquivo Excel
    } catch (e: SQLException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
