// SPDX-License-Identifier: MIT
// Converted with NBH Algorithm nbh_function HollowEngine()

data class InvestmentPlan(
    var initialInvestment: BigInteger,
    var monthlyReturn: BigInteger,
    var annualReturn: BigInteger,
    var netAnnualReturn: BigInteger,
    var slots: BigInteger
)

class InvestmentContract(private val owner: String) {
    private val balances: MutableMap<String, BigInteger> = mutableMapOf()
    private val investedAmount: MutableMap<String, BigInteger> = mutableMapOf()
    private val authorizedInvestors: MutableMap<String, Boolean> = mutableMapOf()

    fun invest(investor: String, amount: BigInteger) {
        require(authorizedInvestors[investor] == true) { "Investor is not authorized" }
        require(amount > BigInteger.ZERO) { "Investment amount must be greater than zero" }
        require(amount <= (balances[investor] ?: BigInteger.ZERO)) { "Insufficient balance" }

        balances[investor] = balances[investor]!!.subtract(amount)
        investedAmount[investor] = investedAmount[investor]?.add(amount) ?: amount
        emitInvestment(investor, amount)
    }

    fun authorizeInvestor(investor: String, authorized: Boolean) {
        require(owner == investor) { "Only owner can perform this action" }
        authorizedInvestors[investor] = authorized
        emitAuthorizationChanged(investor, authorized)
    }

    fun balanceOf(investor: String): BigInteger {
        return balances[investor] ?: BigInteger.ZERO
    }

    fun investedAmountOf(investor: String): BigInteger {
        return investedAmount[investor] ?: BigInteger.ZERO
    }

    private fun emitInvestment(investor: String, amount: BigInteger) {
        // Emit Investment event (placeholder)
    }

    private fun emitAuthorizationChanged(investor: String, authorized: Boolean) {
        // Emit AuthorizationChanged event (placeholder)
    }
}

class AuthenticationContract(private val owner: String) {
    private val messageAuthenticity: MutableMap<String, MutableMap<String, Boolean>> = mutableMapOf()

    fun authenticateMessage(investor: String, messageHash: String) {
        require(owner == investor) { "Only owner can perform this action" }
        require(messageAuthenticity.getOrDefault(investor, mutableMapOf())[messageHash] != true) { "Message already authenticated" }

        messageAuthenticity.getOrPut(investor) { mutableMapOf() }[messageHash] = true
        emitMessageAuthenticated(investor, messageHash, true)
    }

    fun isMessageAuthenticated(investor: String, messageHash: String): Boolean {
        return messageAuthenticity.getOrDefault(investor, mutableMapOf())[messageHash] == true
    }

    private fun emitMessageAuthenticated(investor: String, messageHash: String, authenticated: Boolean) {
        // Emit MessageAuthenticated event (placeholder)
    }
}

class InvestmentManager(
    private val investmentContract: InvestmentContract,
    private val authenticationContract: AuthenticationContract
) {
    private val investmentPlans: MutableMap<String, InvestmentPlan> = mutableMapOf()

    fun addPlan(planName: String, initialInvestment: BigInteger, monthlyReturn: BigInteger, annualReturn: BigInteger, netAnnualReturn: BigInteger, slots: BigInteger) {
        investmentPlans[planName] = InvestmentPlan(initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots)
        emitPlanAdded(planName, initialInvestment, monthlyReturn, annualReturn, netAnnualReturn, slots)
    }

    fun invest(planName: String, amount: BigInteger, investor: String) {
        require(amount > BigInteger.ZERO) { "Investment amount must be greater than zero" }
        // Logic for investing according to your system can be implemented here.
        investmentContract.invest(investor, amount)
    }

    fun getInvestmentContractBalance(investor: String): BigInteger {
        return investmentContract.balanceOf(investor)
    }

    fun authenticateMessage(investor: String, messageHash: String) {
        authenticationContract.authenticateMessage(investor, messageHash)
    }

    fun isMessageAuthenticated(investor: String, messageHash: String): Boolean {
        return authenticationContract.isMessageAuthenticated(investor, messageHash)
    }

    private fun emitPlanAdded(planName: String, initialInvestment: BigInteger, monthlyReturn: BigInteger, annualReturn: BigInteger, netAnnualReturn: BigInteger, slots: BigInteger) {
        // Emit PlanAdded event (placeholder)
    }
}
