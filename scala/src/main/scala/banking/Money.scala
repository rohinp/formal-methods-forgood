package banking

opaque type Balance = Int

object Balance:
  enum CreationError:
    case NegativeBalance

  def from(value: Int): Either[CreationError, Balance] =
    Either.cond(value >= 0, value, CreationError.NegativeBalance)

  extension (balance: Balance) def value: Int = balance

opaque type WithdrawalAmount = Int

object WithdrawalAmount:
  enum CreationError:
    case NegativeWithdrawalAmount

  def from(value: Int): Either[CreationError, WithdrawalAmount] =
    Either.cond(value >= 0, value, CreationError.NegativeWithdrawalAmount)

  extension (amount: WithdrawalAmount) def value: Int = amount

