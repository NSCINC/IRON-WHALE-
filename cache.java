import java.util.HashMap;
import java.util.Map;

public class InvestmentManager {
    // Estrutura para armazenar detalhes do plano de investimento
    static class InvestmentPlan {
        int initialInvestment;
        int monthlyReturn;
        int annualReturn;
        int netAnnualReturn;
        int slots;

        InvestmentPlan(int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
            this.initialInvestment = initialInvestment;
            this.monthlyReturn = monthlyReturn;
            this.annualReturn = annualReturn;
            this.netAnnualReturn = netAnnualReturn;
            this.slots = slots;
        }
    }

    // Mapeamento para armazenar dados em cache
    private static final Map<String, InvestmentPlan> cache = new HashMap<>();

    // Função para adicionar um plano de investimento ao cache
    public static void addInvestmentPlan(String planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
        System.out.println("Adding plan: " + planName);
        if (cache.containsKey(planName)) {
            throw new IllegalArgumentException("Plan already exists");
        }
        cache.put(planName, new InvestmentPlan(initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots));
        System.out.println("Plan added: " + planName);
    }

    // Função para atualizar um plano de investimento no cache
    public static void updateInvestmentPlan(String planName, Integer initialInvestment, Integer monthlyReturn, Integer annualReturn, Integer netAnnualReturn, Integer slots) {
        System.out.println("Updating plan: " + planName);
        InvestmentPlan plan = cache.get(planName);
        if (plan == null) {
            throw new IllegalArgumentException("Plan does not exist");
        }
        if (initialInvestment != null) plan.initialInvestment = initialInvestment;
        if (monthlyReturn != null) plan.monthlyReturn = monthlyReturn;
        if (annualReturn != null) plan.annualReturn = annualReturn;
        if (netAnnualReturn != null) plan.netAnnualReturn = netAnnualReturn;
        if (slots != null) plan.slots = slots;
        System.out.println("Plan updated: " + planName);
    }

    // Função para remover um plano de investimento do cache
    public static void removeInvestmentPlan(String planName) {
        System.out.println("Removing plan: " + planName);
        if (!cache.containsKey(planName)) {
            throw new IllegalArgumentException("Plan does not exist");
        }
        cache.remove(planName);
        System.out.println("Plan removed: " + planName);
    }

    // Função para consultar os detalhes de um plano de investimento no cache
    public static InvestmentPlan getInvestmentPlanDetails(String planName) {
        System.out.println("Fetching details for plan: " + planName);
        InvestmentPlan plan = cache.get(planName);
        if (plan == null) {
            throw new IllegalArgumentException("Plan does not exist");
        }
        return plan;
    }

    // Função para listar todos os planos de investimento
    public static void listInvestmentPlans() {
        System.out.println("Listing all investment plans:");
        for (Map.Entry<String, InvestmentPlan> entry : cache.entrySet()) {
            String planName = entry.getKey();
            InvestmentPlan plan = entry.getValue();
            System.out.printf("Plan Name: %s%n", planName);
            System.out.printf("  Initial Investment: %d%n", plan.initialInvestment);
            System.out.printf("  Monthly Return: %d%n", plan.monthlyReturn);
            System.out.printf("  Annual Return: %d%n", plan.annualReturn);
            System.out.printf("  Net Annual Return: %d%n", plan.netAnnualReturn);
            System.out.printf("  Slots: %d%n", plan.slots);
            System.out.println();
        }
    }

    // Exemplo de uso das funções definidas
    public static void main(String[] args) {
        // Etapa 1: Adicionar um plano de investimento
        System.out.println("\nStep 1: Adding an Investment Plan");
        addInvestmentPlan("economicPlan", 500, 5, 60, 300, 500);

        // Etapa 2: Consultar detalhes de um plano de investimento
        System.out.println("\nStep 2: Fetching Investment Plan Details");
        InvestmentPlan plan = getInvestmentPlanDetails("economicPlan");
        System.out.printf("Initial Investment: %d%n", plan.initialInvestment);
        System.out.printf("Monthly Return: %d%n", plan.monthlyReturn);
        System.out.printf("Annual Return: %d%n", plan.annualReturn);
        System.out.printf("Net Annual Return: %d%n", plan.netAnnualReturn);
        System.out.printf("Slots: %d%n", plan.slots);

        // Etapa 3: Atualizar um plano de investimento
        System.out.println("\nStep 3: Updating an Investment Plan");
        updateInvestmentPlan("economicPlan", null, 10, null, 400, null);

        // Etapa 4: Consultar detalhes do plano de investimento após atualização
        System.out.println("\nStep 4: Fetching Updated Investment Plan Details");
        plan = getInvestmentPlanDetails("economicPlan");
        System.out.printf("Initial Investment: %d%n", plan.initialInvestment);
        System.out.printf("Monthly Return: %d%n", plan.monthlyReturn);
        System.out.printf("Annual Return: %d%n", plan.annualReturn);
        System.out.printf("Net Annual Return: %d%n", plan.netAnnualReturn);
        System.out.printf("Slots: %d%n", plan.slots);

        // Etapa 5: Listar todos os planos de investimento
        System.out.println("\nStep 5: Listing All Investment Plans");
        listInvestmentPlans();

        // Etapa 6: Remover um plano de investimento
        System.out.println("\nStep 6: Removing an Investment Plan");
        removeInvestmentPlan("economicPlan");

        // Etapa 7: Listar todos os planos de investimento após remoção
        System.out.println("\nStep 7: Listing All Investment Plans After Removal");
        listInvestmentPlans();

        // Fim dos exemplos
        System.out.println("\nJava code execution completed.");
    }
}
