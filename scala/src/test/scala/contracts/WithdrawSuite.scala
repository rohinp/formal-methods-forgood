package contracts

import banking.Withdraw
import banking.Withdraw.WithdrawalError
import banking.{Balance, WithdrawalAmount}
import banking.Balance.*

class WithdrawSuite extends munit.FunSuite:
  private def balance(value: Int): Balance = Balance.from(value).toOption.get
  private def amount(value: Int): WithdrawalAmount =
    WithdrawalAmount.from(value).toOption.get

  test("withdraws part of a balance"):
    assertEquals(Withdraw.withdraw(balance(100), amount(30)).map(_.value), Right(70))

  test("may withdraw nothing"):
    assertEquals(Withdraw.withdraw(balance(100), amount(0)).map(_.value), Right(100))

  test("may withdraw the entire balance"):
    assertEquals(Withdraw.withdraw(balance(100), amount(100)).map(_.value), Right(0))

  test("rejects an overdraft"):
    assertEquals(
      Withdraw.withdraw(balance(100), amount(101)),
      Left(WithdrawalError.AmountExceedsBalance)
    )
