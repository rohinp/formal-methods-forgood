package contracts

object Withdraw:
  def withdraw(balance: Int, amount: Int): Int =
    require(balance >= 0, "balance must be non-negative")
    require(amount >= 0, "amount must be non-negative")
    require(amount <= balance, "amount must not exceed balance")

    val newBalance = balance - amount

    // `ensuring` checks promises made by the implementation.
    newBalance.ensuring(_ >= 0, "result must be non-negative")
      .ensuring(_ == balance - amount, "result must equal balance - amount")

