#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sqlite3.h>

void initialize_database() {
    sqlite3 *db;
    char *errMsg = 0;

    // Open SQLite database
    int rc = sqlite3_open("investments.db", &db);
    if (rc) {
        fprintf(stderr, "Can't open database: %s\n", sqlite3_errmsg(db));
        return;
    }

    // SQL commands to create tables
    const char *create_investors_table = 
        "CREATE TABLE IF NOT EXISTS Investors ("
        "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "name TEXT NOT NULL, "
        "email TEXT NOT NULL, "
        "phone_number TEXT NOT NULL);";

    const char *create_assets_table = 
        "CREATE TABLE IF NOT EXISTS Assets ("
        "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "asset_name TEXT NOT NULL, "
        "asset_type TEXT NOT NULL, "
        "total_slots INTEGER NOT NULL, "
        "annual_return REAL NOT NULL);";

    const char *create_investments_table = 
        "CREATE TABLE IF NOT EXISTS Investments ("
        "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        "investor_id INTEGER, "
        "asset_id INTEGER, "
        "invested_amount REAL, "
        "FOREIGN KEY(investor_id) REFERENCES Investors(id), "
        "FOREIGN KEY(asset_id) REFERENCES Assets(id));";

    // Execute SQL statements
    sqlite3_exec(db, create_investors_table, 0, 0, &errMsg);
    sqlite3_exec(db, create_assets_table, 0, 0, &errMsg);
    sqlite3_exec(db, create_investments_table, 0, 0, &errMsg);

    // Check for errors
    if (errMsg) {
        fprintf(stderr, "SQL error: %s\n", errMsg);
        sqlite3_free(errMsg);
    }

    // Close the database
    sqlite3_close(db);
}

void read_csv_data(const char *file_path) {
    FILE *file = fopen(file_path, "r");
    if (!file) {
        perror("Could not open file");
        return;
    }

    char line[256];
    while (fgets(line, sizeof(line), file)) {
        char *name = strtok(line, ",");
        char *email = strtok(NULL, ",");
        char *phone = strtok(NULL, ",");

        if (name && email && phone) {
            // Trim newline characters
            name[strcspn(name, "\n")] = 0;
            email[strcspn(email, "\n")] = 0;
            phone[strcspn(phone, "\n")] = 0;

            // Here you can insert data into the database
            printf("Name: %s, Email: %s, Phone: %s\n", name, email, phone);
        }
    }

    fclose(file);
}

int main() {
    initialize_database();
    read_csv_data("investors_data.csv");
    return 0;
}
