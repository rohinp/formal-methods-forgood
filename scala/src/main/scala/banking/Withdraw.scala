package banking

object Withdraw:
  enum WithdrawalError:
    case AmountExceedsBalance

  def withdraw(
      balance: Balance,
      amount: WithdrawalAmount
  ): Either[WithdrawalError, Balance] =
    if amount.value > balance.value then Left(WithdrawalError.AmountExceedsBalance)
    else
      val rawNewBalance = balance.value - amount.value

      // A failed postcondition is an implementation defect, not a domain error.
      val checked = rawNewBalance
        .ensuring(_ >= 0, "result must be non-negative")
        .ensuring(
          _ == balance.value - amount.value,
          "result must equal balance - amount"
        )

      Balance.from(checked) match
        case Right(newBalance) => Right(newBalance)
        case Left(_) => throw AssertionError("postcondition failed: invalid balance")
