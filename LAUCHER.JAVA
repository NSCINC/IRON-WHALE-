import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        // Conexão com o PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/your_database";
        String user = "your_username";
        String password = "your_password";
        
        // SQL para criar tabelas users, crm_data e erp_data
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                                  "id SERIAL PRIMARY KEY, " +
                                  "name VARCHAR(100) NOT NULL, " +
                                  "email VARCHAR(100) NOT NULL, " +
                                  "token VARCHAR(255));";
        
        String createCRMDataTable = "CREATE TABLE IF NOT EXISTS crm_data (" +
                                    "id SERIAL PRIMARY KEY, " +
                                    "user_id INTEGER REFERENCES users(id), " +
                                    "data JSONB, " +
                                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        String createERPDataTable = "CREATE TABLE IF NOT EXISTS erp_data (" +
                                    "id SERIAL PRIMARY KEY, " +
                                    "user_id INTEGER REFERENCES users(id), " +
                                    "data JSONB, " +
                                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            // Executa os comandos de criação das tabelas
            stmt.execute(createUsersTable);
            stmt.execute(createCRMDataTable);
            stmt.execute(createERPDataTable);
        } catch (SQLException e) {
            System.out.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExcelReader {
    public static void readExcelData(String filePath) {
        String url = "jdbc:postgresql://localhost:5432/your_database";
        String user = "your_username";
        String password = "your_password";

        String insertUserSQL = "INSERT INTO users (name, email, token) VALUES (?, ?, ?)";

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis);
             Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {

            Sheet sheet = workbook.getSheetAt(0); // Lê a primeira planilha

            for (Row row : sheet) {
                Cell nameCell = row.getCell(0);  // Nome
                Cell emailCell = row.getCell(1); // Email
                Cell tokenCell = row.getCell(2); // Token

                String name = nameCell.getStringCellValue();
                String email = emailCell.getStringCellValue();
                String token = tokenCell.getStringCellValue();

                // Inserindo dados na tabela users
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, token);
                pstmt.executeUpdate();

                System.out.println("Dados inseridos: Nome: " + name + ", Email: " + email + ", Token: " + token);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readExcelData("investors_data.xlsx");
    }
}
