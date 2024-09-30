#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <stdint.h>

#define MAX_INVESTORS 100
#define MAX_PLANS 100
#define HASH_SIZE 256

typedef struct {
    int64_t initialInvestment;
    int64_t monthlyReturn;
    int64_t annualReturn;
    int64_t netAnnualReturn;
    int64_t slots;
} InvestmentPlan;

typedef struct {
    char investors[MAX_INVESTORS][50]; // Array de investidores
    int64_t balances[MAX_INVESTORS];
    int64_t investedAmount[MAX_INVESTORS];
    bool authorizedInvestors[MAX_INVESTORS];
    int investorCount;
    char owner[50];
} InvestmentContract;

// Estrutura para armazenar a autenticidade da mensagem
typedef struct {
    char owner[50];
    char messageHashes[MAX_INVESTORS][HASH_SIZE]; // Armazenar hashes de mensagens
    bool isAuthenticated[MAX_INVESTORS]; // Autenticidade da mensagem
    int hashCount;
} AuthenticationContract;

typedef struct {
    InvestmentPlan investmentPlans[MAX_PLANS];
    char planNames[MAX_PLANS][50];
    int planCount;
    InvestmentContract *investmentContract;
    AuthenticationContract *authenticationContract;
} InvestmentManager;

// Funções para o InvestmentContract
void initializeInvestmentContract(InvestmentContract *contract, const char *owner) {
    strcpy(contract->owner, owner);
    contract->investorCount = 0;
    memset(contract->balances, 0, sizeof(contract->balances));
    memset(contract->investedAmount, 0, sizeof(contract->investedAmount));
    memset(contract->authorizedInvestors, false, sizeof(contract->authorizedInvestors));
}

void invest(InvestmentContract *contract, const char *investor, int64_t amount) {
    int investorIndex = -1;
    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            investorIndex = i;
            break;
        }
    }

    // Verificar se o investidor está autorizado
    if (investorIndex == -1 || !contract->authorizedInvestors[investorIndex]) {
        printf("Investor is not authorized\n");
        return;
    }

    if (amount <= 0) {
        printf("Investment amount must be greater than zero\n");
        return;
    }

    if (amount > contract->balances[investorIndex]) {
        printf("Insufficient balance\n");
        return;
    }

    contract->balances[investorIndex] -= amount;
    contract->investedAmount[investorIndex] += amount;
    printf("Investment of %ld made by %s\n", amount, investor);
}

void authorizeInvestor(InvestmentContract *contract, const char *investor, bool authorized) {
    if (strcmp(contract->owner, investor) != 0) {
        printf("Only owner can perform this action\n");
        return;
    }

    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            contract->authorizedInvestors[i] = authorized;
            printf("Authorization changed for %s: %d\n", investor, authorized);
            return;
        }
    }
}

int64_t balanceOf(InvestmentContract *contract, const char *investor) {
    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            return contract->balances[i];
        }
    }
    return 0;
}

int64_t investedAmountOf(InvestmentContract *contract, const char *investor) {
    for (int i = 0; i < contract->investorCount; i++) {
        if (strcmp(contract->investors[i], investor) == 0) {
            return contract->investedAmount[i];
        }
    }
    return 0;
}

// Funções para o AuthenticationContract
void initializeAuthenticationContract(AuthenticationContract *authContract, const char *owner) {
    strcpy(authContract->owner, owner);
    authContract->hashCount = 0;
    memset(authContract->messageHashes, 0, sizeof(authContract->messageHashes));
    memset(authContract->isAuthenticated, false, sizeof(authContract->isAuthenticated));
}

void authenticateMessage(AuthenticationContract *authContract, const char *investor, const char *messageHash) {
    if (strcmp(authContract->owner, investor) != 0) {
        printf("Only owner can perform this action\n");
        return;
    }

    for (int i = 0; i < authContract->hashCount; i++) {
        if (strcmp(authContract->messageHashes[i], messageHash) == 0 && authContract->isAuthenticated[i]) {
            printf("Message already authenticated\n");
            return;
        }
    }

    strcpy(authContract->messageHashes[authContract->hashCount], messageHash);
    authContract->isAuthenticated[authContract->hashCount] = true;
    authContract->hashCount++;
    printf("Message authenticated: %s\n", messageHash);
}

bool isMessageAuthenticated(AuthenticationContract *authContract, const char *messageHash) {
    for (int i = 0; i < authContract->hashCount; i++) {
        if (strcmp(authContract->messageHashes[i], messageHash) == 0 && authContract->isAuthenticated[i]) {
            return true;
        }
    }
    return false;
}

// Funções para o InvestmentManager
void initializeInvestmentManager(InvestmentManager *manager, InvestmentContract *investmentContract, AuthenticationContract *authenticationContract) {
    manager->investmentContract = investmentContract;
    manager->authenticationContract = authenticationContract;
    manager->planCount = 0;
}

void addPlan(InvestmentManager *manager, const char *planName, int64_t initialInvestment, int64_t monthlyReturn, int64_t annualReturn, int64_t netAnnualReturn, int64_t slots) {
    InvestmentPlan plan;
    plan.initialInvestment = initialInvestment;
    plan.monthlyReturn = monthlyReturn;
    plan.annualReturn = annualReturn;
    plan.netAnnualReturn = netAnnualReturn;
    plan.slots = slots;

    strcpy(manager->planNames[manager->planCount], planName);
    manager->investmentPlans[manager->planCount] = plan;
    manager->planCount++;
    printf("Plan added: %s\n", planName);
}

void investInPlan(InvestmentManager *manager, const char *planName, int64_t amount, const char *investor) {
    invest(manager->investmentContract, investor, amount);
}

int64_t getInvestmentContractBalance(InvestmentManager *manager, const char *investor) {
    return balanceOf(manager->investmentContract, investor);
}

void authenticateMessageInManager(InvestmentManager *manager, const char *investor, const char *messageHash) {
    authenticateMessage(manager->authenticationContract, investor, messageHash);
}

bool isMessageAuthenticatedInManager(InvestmentManager *manager, const char *messageHash) {
    return isMessageAuthenticated(manager->authenticationContract, messageHash);
}

// Função principal para teste
int main() {
    InvestmentContract investmentContract;
    initializeInvestmentContract(&investmentContract, "owner123");

    // Adicionando investidores
    strcpy(investmentContract.investors[0], "investor1");
    investmentContract.balances[0] = 10000;
    investmentContract.authorizedInvestors[0] = true;
    investmentContract.investorCount++;

    strcpy(investmentContract.investors[1], "investor2");
    investmentContract.balances[1] = 5000;
    investmentContract.authorizedInvestors[1] = false;
    investmentContract.investorCount++;

    AuthenticationContract authenticationContract;
    initializeAuthenticationContract(&authenticationContract, "owner123");

    InvestmentManager investmentManager;
    initializeInvestmentManager(&investmentManager, &investmentContract, &authenticationContract);

    addPlan(&investmentManager, "Plan A", 1000, 100, 1200, 1100, 10);
    investInPlan(&investmentManager, "Plan A", 500, "investor1");
    printf("Investor1 balance: %ld\n", getInvestmentContractBalance(&investmentManager, "investor1"));

    authenticateMessageInManager(&investmentManager, "owner123", "message_hash_1");
    printf("Is message authenticated: %d\n", isMessageAuthenticatedInManager(&investmentManager, "message_hash_1"));

    return 0;
}
