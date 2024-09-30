import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String url = "jdbc:sqlite:investments.db";
        
        // SQL para criar tabelas
        String createInvestorsTable = "CREATE TABLE IF NOT EXISTS Investors (" +
                                      "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                      "name TEXT NOT NULL, " +
                                      "email TEXT NOT NULL, " +
                                      "phone_number TEXT NOT NULL);";
        
        String createAssetsTable = "CREATE TABLE IF NOT EXISTS Assets (" +
                                   "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                   "asset_name TEXT NOT NULL, " +
                                   "asset_type TEXT NOT NULL, " +
                                   "total_slots INTEGER NOT NULL, " +
                                   "annual_return REAL NOT NULL);";

        String createInvestmentsTable = "CREATE TABLE IF NOT EXISTS Investments (" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                        "investor_id INTEGER, " +
                                        "asset_id INTEGER, " +
                                        "invested_amount REAL, " +
                                        "FOREIGN KEY(investor_id) REFERENCES Investors(id), " +
                                        "FOREIGN KEY(asset_id) REFERENCES Assets(id));";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // Executa os comandos de criação das tabelas
            stmt.execute(createInvestorsTable);
            stmt.execute(createAssetsTable);
            stmt.execute(createInvestmentsTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

public class ExcelReader {
    public static void readExcelData(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // Lê a primeira planilha
            for (Row row : sheet) {
                Cell nameCell = row.getCell(0); // Nome
                Cell emailCell = row.getCell(1); // Email
                Cell phoneCell = row.getCell(2); // Telefone
                
                String name = nameCell.getStringCellValue();
                String email = emailCell.getStringCellValue();
                String phone = phoneCell.getStringCellValue();
                
                // Aqui você pode inserir os dados no banco de dados
                System.out.println("Nome: " + name + ", Email: " + email + ", Telefone: " + phone);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readExcelData("investors_data.xlsx");
    }
}
