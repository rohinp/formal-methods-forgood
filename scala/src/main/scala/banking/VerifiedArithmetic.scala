package banking

object VerifiedArithmetic:
  def calculateNewBalance(balance: Int, amount: Int): Int = {
    require(balance >= 0)
    require(amount >= 0)
    require(amount <= balance)
    balance - amount
  }.ensuring(result => result >= 0 && result == balance - amount)
