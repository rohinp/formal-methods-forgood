package invariants

import banking.{Balance, BankAccount, WithdrawalAmount}
import banking.Withdraw.WithdrawalError.AmountExceedsBalance

class BankAccountSuite extends munit.FunSuite:
  private def balance(value: Int): Balance = Balance.from(value).toOption.get
  private def amount(value: Int): WithdrawalAmount =
    WithdrawalAmount.from(value).toOption.get

  test("a new account starts in a consistent state"):
    val account = BankAccount.open(balance(100))

    assertEquals(account.balance.value, 100)
    assertEquals(account.withdrawals, Vector.empty)
    assertEquals(
      account.balance.value + account.withdrawals.map(_.value).sum,
      account.openingBalance.value
    )

  test("successful withdrawals preserve the invariant"):
    val result = for
      afterFirst <- BankAccount.open(balance(100)).withdraw(amount(25))
      afterSecond <- afterFirst.withdraw(amount(15))
    yield afterSecond

    val account = result.toOption.get
    assertEquals(account.balance.value, 60)
    assertEquals(account.withdrawals.map(_.value), Vector(25, 15))
    assertEquals(
      account.balance.value + account.withdrawals.map(_.value).sum,
      account.openingBalance.value
    )

  test("a failed withdrawal leaves the immutable account unchanged"):
    val original = BankAccount.open(balance(100))
    val result = original.withdraw(amount(101))

    assertEquals(result, Left(AmountExceedsBalance))
    assertEquals(original.balance.value, 100)
    assertEquals(original.withdrawals, Vector.empty)

  test("a successful withdrawal returns a new account"):
    val original = BankAccount.open(balance(100))
    val updated = original.withdraw(amount(25)).toOption.get

    assertEquals(original.balance.value, 100)
    assertEquals(original.withdrawals, Vector.empty)
    assertEquals(updated.balance.value, 75)
