#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sqlite3.h>

typedef struct {
    char name[100];
    char email[100];
    char phoneNumber[20];
} Investor;

int initializeDatabase(sqlite3 *db) {
    char *errMsg = 0;
    const char *sql = 
        "CREATE TABLE IF NOT EXISTS Investors ("
        "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "name TEXT NOT NULL, "
        "email TEXT NOT NULL, "
        "phone_number TEXT NOT NULL);";

    return sqlite3_exec(db, sql, 0, 0, &errMsg);
}

int insertInvestor(sqlite3 *db, Investor investor) {
    sqlite3_stmt *stmt;
    const char *sql = "INSERT INTO Investors (name, email, phone_number) VALUES (?, ?, ?)";
    sqlite3_prepare_v2(db, sql, -1, &stmt, 0);

    sqlite3_bind_text(stmt, 1, investor.name, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 2, investor.email, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 3, investor.phoneNumber, -1, SQLITE_STATIC);

    int rc = sqlite3_step(stmt);
    sqlite3_finalize(stmt);
    return rc;
}

void readCsvData(const char *filePath, sqlite3 *db) {
    FILE *file = fopen(filePath, "r");
    if (!file) {
        perror("Failed to open file");
        return;
    }

    char line[256];
    while (fgets(line, sizeof(line), file)) {
        Investor investor;
        sscanf(line, "%99[^,],%99[^,],%19[^\n]", investor.name, investor.email, investor.phoneNumber);
        insertInvestor(db, investor);
    }
    fclose(file);
}

int main() {
    sqlite3 *db;
    int exit = sqlite3_open("investments.db", &db);
    if (exit) {
        fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
        return exit;
    }

    if (initializeDatabase(db) != SQLITE_OK) {
        fprintf(stderr, "Failed to create table: %s\n", sqlite3_errmsg(db));
        return 1;
    }

    readCsvData("investors_data.csv", db); // Coloque o caminho do seu arquivo CSV

    sqlite3_close(db);
    return 0;
}
