package banking

private enum class WithdrawalDecision {
    ALLOWED,
    OVERDRAFT,
}

private fun decide(balance: Balance, amount: WithdrawalAmount): WithdrawalDecision =
    if (amount.value <= balance.value) {
        WithdrawalDecision.ALLOWED
    } else {
        WithdrawalDecision.OVERDRAFT
    }

fun withdraw(balance: Balance, amount: WithdrawalAmount): Balance =
    when (decide(balance, amount)) {
        WithdrawalDecision.OVERDRAFT ->
            throw IllegalArgumentException("amount must not exceed balance")

        WithdrawalDecision.ALLOWED -> {
            val newBalance = Balance(balance.value - amount.value)

            // `check` expresses promises made by the implementation.
            check(newBalance.value >= 0) {
                "postcondition failed: result must be non-negative"
            }
            check(newBalance.value == balance.value - amount.value) {
                "postcondition failed: result must equal balance - amount"
            }

            newBalance
        }
    }
