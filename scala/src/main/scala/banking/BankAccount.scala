package banking

final class BankAccount private (
    val openingBalance: Balance,
    val balance: Balance,
    val withdrawals: Vector[WithdrawalAmount]
):
  def withdraw(amount: WithdrawalAmount): Either[Withdraw.WithdrawalError, BankAccount] =
    Withdraw.withdraw(balance, amount).map { newBalance =>
      BankAccount.validated(openingBalance, newBalance, withdrawals :+ amount)
    }

object BankAccount:
  def open(openingBalance: Balance): BankAccount =
    validated(openingBalance, openingBalance, Vector.empty)

  private def validated(
      openingBalance: Balance,
      balance: Balance,
      withdrawals: Vector[WithdrawalAmount]
  ): BankAccount =
    val withdrawnTotal = withdrawals.foldLeft(0)(_ + _.value)
    assert(
      balance.value + withdrawnTotal == openingBalance.value,
      "account invariant failed: balance plus withdrawals must equal opening balance"
    )
    new BankAccount(openingBalance, balance, withdrawals)
