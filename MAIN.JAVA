#include <stdio.h>
#include <stdlib.h>
#include <sqlite3.h>

// Função para criar a tabela de controle de ativos financeiros
void criar_tabela(sqlite3 *db) {
    const char *sql = "CREATE TABLE IF NOT EXISTS ativos ("
                      "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                      "nome TEXT, "
                      "valor REAL, "
                      "tipo TEXT, "
                      "data_aquisicao TEXT, "
                      "descricao TEXT);";
    
    char *err_msg = NULL;
    int rc = sqlite3_exec(db, sql, 0, 0, &err_msg);
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao criar tabela: %s\n", err_msg);
        sqlite3_free(err_msg);
    } else {
        printf("Tabela de ativos financeiros criada com sucesso.\n");
    }
}

// Função para adicionar um ativo financeiro
void adicionar_ativo(sqlite3 *db, const char *nome, double valor, const char *tipo, const char *data_aquisicao, const char *descricao) {
    sqlite3_stmt *stmt;
    const char *sql = "INSERT INTO ativos (nome, valor, tipo, data_aquisicao, descricao) VALUES (?, ?, ?, ?, ?);";
    
    int rc = sqlite3_prepare_v2(db, sql, -1, &stmt, 0);
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao preparar a instrução: %s\n", sqlite3_errmsg(db));
        return;
    }
    
    sqlite3_bind_text(stmt, 1, nome, -1, SQLITE_STATIC);
    sqlite3_bind_double(stmt, 2, valor);
    sqlite3_bind_text(stmt, 3, tipo, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 4, data_aquisicao, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 5, descricao, -1, SQLITE_STATIC);
    
    rc = sqlite3_step(stmt);
    if (rc != SQLITE_DONE) {
        fprintf(stderr, "Erro ao adicionar ativo: %s\n", sqlite3_errmsg(db));
    } else {
        printf("Ativo adicionado com sucesso.\n");
    }
    
    sqlite3_finalize(stmt);
}

// Função para atualizar um ativo financeiro
void atualizar_ativo(sqlite3 *db, int id, const char *nome, double valor, const char *tipo, const char *data_aquisicao, const char *descricao) {
    sqlite3_stmt *stmt;
    const char *sql = "UPDATE ativos SET nome = ?, valor = ?, tipo = ?, data_aquisicao = ?, descricao = ? WHERE id = ?;";
    
    int rc = sqlite3_prepare_v2(db, sql, -1, &stmt, 0);
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao preparar a instrução: %s\n", sqlite3_errmsg(db));
        return;
    }
    
    sqlite3_bind_text(stmt, 1, nome, -1, SQLITE_STATIC);
    sqlite3_bind_double(stmt, 2, valor);
    sqlite3_bind_text(stmt, 3, tipo, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 4, data_aquisicao, -1, SQLITE_STATIC);
    sqlite3_bind_text(stmt, 5, descricao, -1, SQLITE_STATIC);
    sqlite3_bind_int(stmt, 6, id);
    
    rc = sqlite3_step(stmt);
    if (rc != SQLITE_DONE) {
        fprintf(stderr, "Erro ao atualizar ativo: %s\n", sqlite3_errmsg(db));
    } else {
        printf("Ativo atualizado com sucesso.\n");
    }
    
    sqlite3_finalize(stmt);
}

// Função para visualizar todos os ativos financeiros
void visualizar_ativos(sqlite3 *db) {
    const char *sql = "SELECT * FROM ativos;";
    sqlite3_stmt *stmt;
    
    int rc = sqlite3_prepare_v2(db, sql, -1, &stmt, 0);
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao preparar a instrução: %s\n", sqlite3_errmsg(db));
        return;
    }
    
    while ((rc = sqlite3_step(stmt)) == SQLITE_ROW) {
        printf("ID: %d, Nome: %s, Valor: %.2f, Tipo: %s, Data: %s, Descrição: %s\n",
               sqlite3_column_int(stmt, 0),
               sqlite3_column_text(stmt, 1),
               sqlite3_column_double(stmt, 2),
               sqlite3_column_text(stmt, 3),
               sqlite3_column_text(stmt, 4),
               sqlite3_column_text(stmt, 5));
    }
    
    if (rc != SQLITE_DONE) {
        fprintf(stderr, "Erro ao visualizar ativos: %s\n", sqlite3_errmsg(db));
    }
    
    sqlite3_finalize(stmt);
}

// Função para excluir um ativo financeiro
void excluir_ativo(sqlite3 *db, int id) {
    sqlite3_stmt *stmt;
    const char *sql = "DELETE FROM ativos WHERE id = ?;";
    
    int rc = sqlite3_prepare_v2(db, sql, -1, &stmt, 0);
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao preparar a instrução: %s\n", sqlite3_errmsg(db));
        return;
    }
    
    sqlite3_bind_int(stmt, 1, id);
    
    rc = sqlite3_step(stmt);
    if (rc != SQLITE_DONE) {
        fprintf(stderr, "Erro ao excluir ativo: %s\n", sqlite3_errmsg(db));
    } else {
        printf("Ativo excluído com sucesso.\n");
    }
    
    sqlite3_finalize(stmt);
}

// Função para gerar o relatório de ativos financeiros em formato CSV
void gerar_relatorio_csv(sqlite3 *db, const char *caminho_arquivo) {
    FILE *file = fopen(caminho_arquivo, "w");
    if (file == NULL) {
        perror("Erro ao abrir arquivo CSV");
        return;
    }
    
    fprintf(file, "ID,Nome,Valor,Tipo,Data de Aquisição,Descrição\n");
    
    const char *sql = "SELECT * FROM ativos;";
    sqlite3_stmt *stmt;
    
    int rc = sqlite3_prepare_v2(db, sql, -1, &stmt, 0);
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao preparar a instrução: %s\n", sqlite3_errmsg(db));
        fclose(file);
        return;
    }
    
    while ((rc = sqlite3_step(stmt)) == SQLITE_ROW) {
        fprintf(file, "%d,%s,%.2f,%s,%s,%s\n",
                sqlite3_column_int(stmt, 0),
                sqlite3_column_text(stmt, 1),
                sqlite3_column_double(stmt, 2),
                sqlite3_column_text(stmt, 3),
                sqlite3_column_text(stmt, 4),
                sqlite3_column_text(stmt, 5));
    }
    
    if (rc != SQLITE_DONE) {
        fprintf(stderr, "Erro ao gerar relatório CSV: %s\n", sqlite3_errmsg(db));
    }
    
    sqlite3_finalize(stmt);
    fclose(file);
    printf("Relatório CSV gerado com sucesso em: %s\n", caminho_arquivo);
}

// Função principal para inicializar e controlar os ativos
int main() {
    sqlite3 *db;
    int rc = sqlite3_open("ativos_financeiros.db", &db);
    if (rc != SQLITE_OK) {
        fprintf(stderr, "Erro ao abrir banco de dados: %s\n", sqlite3_errmsg(db));
        return 1;
    }

    criar_tabela(db);

    // Exemplos de operação
    adicionar_ativo(db, "Imóvel Comercial", 1000000, "Real Estate", "2024-01-10", "Loja no centro da cidade");
    adicionar_ativo(db, "Ações da Empresa X", 50000, "Ações", "2024-02-15", "Ações compradas na bolsa de valores");

    printf("\nVisualizando todos os ativos financeiros:\n");
    visualizar_ativos(db);

    printf("\nGerando o relatório CSV dos ativos:\n");
    gerar_relatorio_csv(db, "ativos_financeiros.csv");

    // Fechar a conexão com o banco de dados ao finalizar o programa
    sqlite3_close(db);
    return 0;
}
