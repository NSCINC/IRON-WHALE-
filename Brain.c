#include <stdio.h>
#include <string.h>
#include <stdbool.h>

// Estrutura para armazenar detalhes do plano de investimento
typedef struct {
    char planName[50];
    int initialInvestment;
    int monthlyReturn;
    int annualReturn;
    int netAnnualReturn;
    int slots;
} InvestmentPlan;

// Array estático para armazenar os planos de investimento
#define MAX_PLANS 100
InvestmentPlan cache[MAX_PLANS];
int planCount = 0;

// Função para buscar um plano de investimento no cache
int findPlanIndex(const char *planName) {
    for (int i = 0; i < planCount; i++) {
        if (strcmp(cache[i].planName, planName) == 0) {
            return i;
        }
    }
    return -1; // Plano não encontrado
}

// Função para adicionar um plano de investimento ao cache
void addInvestmentPlan(const char *planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
    printf("Adding plan: %s\n", planName);
    if (findPlanIndex(planName) != -1) {
        printf("Error: Plan already exists\n");
        return;
    }
    if (planCount >= MAX_PLANS) {
        printf("Error: Cache is full\n");
        return;
    }
    strcpy(cache[planCount].planName, planName);
    cache[planCount].initialInvestment = initialInvestment;
    cache[planCount].monthlyReturn = monthlyReturn;
    cache[planCount].annualReturn = annualReturn;
    cache[planCount].netAnnualReturn = netAnnualReturn;
    cache[planCount].slots = slots;
    planCount++;
    printf("Plan added: %s\n", planName);
}

// Função para atualizar um plano de investimento no cache
void updateInvestmentPlan(const char *planName, int *initialInvestment, int *monthlyReturn, int *annualReturn, int *netAnnualReturn, int *slots) {
    printf("Updating plan: %s\n", planName);
    int index = findPlanIndex(planName);
    if (index == -1) {
        printf("Error: Plan does not exist\n");
        return;
    }
    if (initialInvestment != NULL) cache[index].initialInvestment = *initialInvestment;
    if (monthlyReturn != NULL) cache[index].monthlyReturn = *monthlyReturn;
    if (annualReturn != NULL) cache[index].annualReturn = *annualReturn;
    if (netAnnualReturn != NULL) cache[index].netAnnualReturn = *netAnnualReturn;
    if (slots != NULL) cache[index].slots = *slots;
    printf("Plan updated: %s\n", planName);
}

// Função para remover um plano de investimento do cache
void removeInvestmentPlan(const char *planName) {
    printf("Removing plan: %s\n", planName);
    int index = findPlanIndex(planName);
    if (index == -1) {
        printf("Error: Plan does not exist\n");
        return;
    }
    for (int i = index; i < planCount - 1; i++) {
        cache[i] = cache[i + 1];
    }
    planCount--;
    printf("Plan removed: %s\n", planName);
}

// Função para consultar os detalhes de um plano de investimento no cache
InvestmentPlan *getInvestmentPlanDetails(const char *planName) {
    printf("Fetching details for plan: %s\n", planName);
    int index = findPlanIndex(planName);
    if (index == -1) {
        printf("Error: Plan does not exist\n");
        return NULL;
    }
    return &cache[index];
}

// Função para listar todos os planos de investimento
void listInvestmentPlans() {
    printf("Listing all investment plans:\n");
    for (int i = 0; i < planCount; i++) {
        printf("Plan Name: %s\n", cache[i].planName);
        printf("  Initial Investment: %d\n", cache[i].initialInvestment);
        printf("  Monthly Return: %d\n", cache[i].monthlyReturn);
        printf("  Annual Return: %d\n", cache[i].annualReturn);
        printf("  Net Annual Return: %d\n", cache[i].netAnnualReturn);
        printf("  Slots: %d\n\n", cache[i].slots);
    }
}

int main() {
    // Etapa 1: Adicionar um plano de investimento
    printf("\nStep 1: Adding an Investment Plan\n");
    addInvestmentPlan("economicPlan", 500, 5, 60, 300, 500);

    // Etapa 2: Consultar detalhes de um plano de investimento
    printf("\nStep 2: Fetching Investment Plan Details\n");
    InvestmentPlan *plan = getInvestmentPlanDetails("economicPlan");
    if (plan != NULL) {
        printf("Initial Investment: %d\n", plan->initialInvestment);
        printf("Monthly Return: %d\n", plan->monthlyReturn);
        printf("Annual Return: %d\n", plan->annualReturn);
        printf("Net Annual Return: %d\n", plan->netAnnualReturn);
        printf("Slots: %d\n", plan->slots);
    }

    // Etapa 3: Atualizar um plano de investimento
    printf("\nStep 3: Updating an Investment Plan\n");
    int newMonthlyReturn = 10;
    int newNetAnnualReturn = 400;
    updateInvestmentPlan("economicPlan", NULL, &newMonthlyReturn, NULL, &newNetAnnualReturn, NULL);

    // Etapa 4: Consultar detalhes do plano de investimento após atualização
    printf("\nStep 4: Fetching Updated Investment Plan Details\n");
    plan = getInvestmentPlanDetails("economicPlan");
    if (plan != NULL) {
        printf("Initial Investment: %d\n", plan->initialInvestment);
        printf("Monthly Return: %d\n", plan->monthlyReturn);
        printf("Annual Return: %d\n", plan->annualReturn);
        printf("Net Annual Return: %d\n", plan->netAnnualReturn);
        printf("Slots: %d\n", plan->slots);
    }

    // Etapa 5: Listar todos os planos de investimento
    printf("\nStep 5: Listing All Investment Plans\n");
    listInvestmentPlans();

    // Etapa 6: Remover um plano de investimento
    printf("\nStep 6: Removing an Investment Plan\n");
    removeInvestmentPlan("economicPlan");

    // Etapa 7: Listar todos os planos de investimento após remoção
    printf("\nStep 7: Listing All Investment Plans After Removal\n");
    listInvestmentPlans();

    return 0;
}
