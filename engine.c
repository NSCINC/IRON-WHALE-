#include "hollow_engine.h"

// Function to create a new HollowEngine instance
HollowEngine* create_engine(const char* investmentAddress, const char* authenticationAddress) {
    HollowEngine* engine = (HollowEngine*)malloc(sizeof(HollowEngine));
    strcpy(engine->investmentContractAddress, investmentAddress);
    strcpy(engine->authenticationContractAddress, authenticationAddress);
    engine->planCount = 0;
    engine->investmentCount = 0;
    return engine;
}

// Function to add an investment plan
void add_plan(HollowEngine* engine, const char* planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
    if (engine->planCount >= MAX_PLANS) {
        printf("Error: Maximum number of plans reached.\n");
        return;
    }
    InvestmentPlan* plan = &engine->plans[engine->planCount++];
    strcpy(plan->planName, planName);
    plan->initialInvestment = initialInvestment;
    plan->monthlyReturn = monthlyReturn;
    plan->annualReturn = annualReturn;
    plan->netAnnualReturn = netAnnualReturn;
    plan->slots = slots;
    printf("Plan added successfully!\n");
}

// Function to invest in a plan
void invest(HollowEngine* engine, const char* planName, int amount, const char* investorAddress) {
    if (engine->investmentCount >= MAX_INVESTMENTS) {
        printf("Error: Maximum number of investments reached.\n");
        return;
    }

    // Check if the plan exists
    for (int i = 0; i < engine->planCount; i++) {
        if (strcmp(engine->plans[i].planName, planName) == 0) {
            Investment* investment = &engine->investments[engine->investmentCount++];
            strcpy(investment->planName, planName);
            investment->amount = amount;
            strcpy(investment->investorAddress, investorAddress);
            printf("Investment completed successfully!\n");
            return;
        }
    }
    printf("Error: Investment plan not found: %s\n", planName);
}

// Function to authenticate a message
void authenticate_message(const char* messageHash) {
    // Simulate message authentication logic
    printf("Message authenticated successfully!\n");
}

