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
      val checked = VerifiedArithmetic.calculateNewBalance(balance.value, amount.value)

      Balance.from(checked) match
        case Right(newBalance) => Right(newBalance)
        case Left(_) => throw AssertionError("postcondition failed: invalid balance")
