package contracts

object Withdraw:
  enum WithdrawalError:
    case NegativeBalance
    case NegativeAmount
    case AmountExceedsBalance

  def withdraw(balance: Int, amount: Int): Either[WithdrawalError, Int] =
    if balance < 0 then Left(WithdrawalError.NegativeBalance)
    else if amount < 0 then Left(WithdrawalError.NegativeAmount)
    else if amount > balance then Left(WithdrawalError.AmountExceedsBalance)
    else
      val newBalance = balance - amount

      // A failed postcondition is an implementation defect, not a domain error.
      Right(
        newBalance.ensuring(_ >= 0, "result must be non-negative")
          .ensuring(_ == balance - amount, "result must equal balance - amount")
      )
