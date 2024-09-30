#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libpq-fe.h>

void readCSVAndInsertIntoDatabase(const char *filePath) {
    FILE *file = fopen(filePath, "r");
    if (!file) {
        printf("Erro ao abrir arquivo CSV\n");
        return;
    }

    // Conexão com o PostgreSQL
    const char *conninfo = "dbname=your_database user=your_username password=your_password hostaddr=127.0.0.1 port=5432";
    PGconn *conn = PQconnectdb(conninfo);

    if (PQstatus(conn) != CONNECTION_OK) {
        printf("Erro de conexão com o banco de dados: %s", PQerrorMessage(conn));
        PQfinish(conn);
        fclose(file);
        return;
    }

    char line[1024];
    while (fgets(line, sizeof(line), file)) {
        char *name = strtok(line, ",");
        char *email = strtok(NULL, ",");
        char *token = strtok(NULL, ",");

        char query[512];
        snprintf(query, sizeof(query), "INSERT INTO users (name, email, token) VALUES ('%s', '%s', '%s')", name, email, token);

        PGresult *res = PQexec(conn, query);
        if (PQresultStatus(res) != PGRES_COMMAND_OK) {
            printf("Erro ao inserir dados: %s\n", PQerrorMessage(conn));
        } else {
            printf("Dados inseridos: Nome: %s, Email: %s, Token: %s\n", name, email, token);
        }
        PQclear(res);
    }

    fclose(file);
    PQfinish(conn);
}

int main() {
    readCSVAndInsertIntoDatabase("investors_data.csv");
    return 0;
}
