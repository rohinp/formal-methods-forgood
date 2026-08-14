package banking

fun withdraw(balance: Balance, amount: WithdrawalAmount): Balance {
    require(amount.value <= balance.value) { "amount must not exceed balance" }

    val newBalance = Balance(balance.value - amount.value)

    // `check` expresses promises made by the implementation.
    check(newBalance.value >= 0) { "postcondition failed: result must be non-negative" }
    check(newBalance.value == balance.value - amount.value) {
        "postcondition failed: result must equal balance - amount"
    }

    return newBalance
}
