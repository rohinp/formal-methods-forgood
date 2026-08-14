package banking

fun withdraw(balance: Int, amount: Int): Int {
    require(balance >= 0) { "balance must be non-negative" }
    require(amount >= 0) { "amount must be non-negative" }
    require(amount <= balance) { "amount must not exceed balance" }

    val newBalance = balance - amount

    // `check` expresses promises made by the implementation.
    check(newBalance >= 0) { "postcondition failed: result must be non-negative" }
    check(newBalance == balance - amount) {
        "postcondition failed: result must equal balance - amount"
    }

    return newBalance
}
