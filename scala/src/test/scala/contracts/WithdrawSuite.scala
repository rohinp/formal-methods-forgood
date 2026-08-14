package contracts

import banking.Withdraw
import banking.Withdraw.WithdrawalError

class WithdrawSuite extends munit.FunSuite:
  test("withdraws part of a balance"):
    assertEquals(Withdraw.withdraw(100, 30), Right(70))

  test("may withdraw nothing"):
    assertEquals(Withdraw.withdraw(100, 0), Right(100))

  test("may withdraw the entire balance"):
    assertEquals(Withdraw.withdraw(100, 100), Right(0))

  test("rejects a negative balance"):
    assertEquals(Withdraw.withdraw(-1, 0), Left(WithdrawalError.NegativeBalance))

  test("rejects a negative amount"):
    assertEquals(Withdraw.withdraw(100, -1), Left(WithdrawalError.NegativeAmount))

  test("rejects an overdraft"):
    assertEquals(Withdraw.withdraw(100, 101), Left(WithdrawalError.AmountExceedsBalance))
