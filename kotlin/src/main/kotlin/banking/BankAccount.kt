package banking

class BankAccount(val openingBalance: Balance) {
    var balance: Balance = openingBalance
        private set

    private val mutableWithdrawals = mutableListOf<WithdrawalAmount>()
    val withdrawals: List<WithdrawalAmount>
        get() = mutableWithdrawals.toList()

    init {
        checkInvariant(balance, mutableWithdrawals)
    }

    fun withdraw(amount: WithdrawalAmount) {
        val candidateBalance = banking.withdraw(balance, amount)
        val candidateWithdrawals = mutableWithdrawals + amount

        // Validate the whole candidate state before changing this object.
        checkInvariant(candidateBalance, candidateWithdrawals)
        balance = candidateBalance
        mutableWithdrawals.add(amount)
    }

    private fun checkInvariant(
        candidateBalance: Balance,
        candidateWithdrawals: List<WithdrawalAmount>,
    ) {
        val withdrawnTotal = candidateWithdrawals.sumOf { it.value }
        check(candidateBalance.value + withdrawnTotal == openingBalance.value) {
            "account invariant failed: balance plus withdrawals must equal opening balance"
        }
    }
}
