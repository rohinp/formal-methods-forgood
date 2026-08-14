package banking

@JvmInline
value class Balance(val value: Int) {
    init {
        require(value >= 0) { "balance must be non-negative" }
    }
}

@JvmInline
value class WithdrawalAmount(val value: Int) {
    init {
        require(value >= 0) { "withdrawal amount must be non-negative" }
    }
}
