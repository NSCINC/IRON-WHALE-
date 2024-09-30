import java.util.HashMap;
import java.util.Map;

public class HollowEngine {
    private String investmentContractAddress;
    private String authenticationContractAddress;
    private Map<String, InvestmentPlan> plans;
    private Map<String, Investment> investments;

    public HollowEngine(String investmentContractAddress, String authenticationContractAddress) {
        this.investmentContractAddress = investmentContractAddress;
        this.authenticationContractAddress = authenticationContractAddress;
        this.plans = new HashMap<>();
        this.investments = new HashMap<>();
    }

    public void addPlan(String planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
        InvestmentPlan plan = new InvestmentPlan(planName, initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots);
        plans.put(planName, plan);
        System.out.println("Plan added successfully!");
    }

    public void invest(String planName, int amount, String investorAddress) {
        if (!plans.containsKey(planName)) {
            throw new IllegalArgumentException("Investment plan not found: " + planName);
        }
        Investment investment = new Investment(planName, amount, investorAddress);
        investments.put(investorAddress, investment);
        System.out.println("Investment completed successfully!");
    }

    public void authenticateMessage(String messageHash) {
        // Simulate message authentication logic
        System.out.println("Message authenticated successfully!");
    }

    // Nested class to represent an investment plan
    private static class InvestmentPlan {
        String planName;
        int initialInvestment;
        int monthlyReturn;
        int annualReturn;
        int netAnnualReturn;
        int slots;

        public InvestmentPlan(String planName, int initialInvestment, int monthlyReturn, int annualReturn, int netAnnualReturn, int slots) {
            this.planName = planName;
            this.initialInvestment = initialInvestment;
            this.monthlyReturn = monthlyReturn;
            this.annualReturn = annualReturn;
            this.netAnnualReturn = netAnnualReturn;
            this.slots = slots;
        }
    }

    // Nested class to represent an investment
    private static class Investment {
        String planName;
        int amount;
        String investorAddress;

        public Investment(String planName, int amount, String investorAddress) {
            this.planName = planName;
            this.amount = amount;
            this.investorAddress = investorAddress;
        }
    }

    public static void main(String[] args) {
        // Endereços de exemplo para contratos simulados
        String investmentContractAddress = "0x1111111111111111111111111111111111111111";
        String authenticationContractAddress = "0x2222222222222222222222222222222222222222";

        // Instanciar HollowEngine com endereços simulados
        HollowEngine engine = new HollowEngine(investmentContractAddress, authenticationContractAddress);

        // Função para adicionar um plano de investimento
        System.out.println("\nStep 1: Adding an Investment Plan");
        engine.addPlan("economicPlan", 500, 5, 60, 300, 500);

        // Função para simular o investimento em um plano de investimento
        System.out.println("\nStep 2: Investing in the economicPlan");
        engine.invest("economicPlan", 100, "0x3333333333333333333333333333333333333333");

        // Função para autenticar uma mensagem
        System.out.println("\nStep 3: Authenticating a Message");
        String messageHash = "0xabcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890";
        engine.authenticateMessage(messageHash);

        // Fim dos testes
        System.out.println("\nKernel test steps completed.");
    }
}
