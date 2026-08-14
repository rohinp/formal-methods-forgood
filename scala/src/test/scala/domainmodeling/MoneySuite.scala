package domainmodeling

import banking.{Balance, WithdrawalAmount}
import banking.Balance.CreationError.NegativeBalance
import banking.WithdrawalAmount.CreationError.NegativeWithdrawalAmount
import scala.compiletime.testing.typeCheckErrors

class MoneySuite extends munit.FunSuite:
  test("balance rejects negative values"):
    assertEquals(Balance.from(-1), Left(NegativeBalance))

  test("withdrawal amount rejects negative values"):
    assertEquals(WithdrawalAmount.from(-1), Left(NegativeWithdrawalAmount))

  test("balance and withdrawal amount are distinct compile-time types"):
    val errors = typeCheckErrors(
      "val balance: banking.Balance = banking.WithdrawalAmount.from(1).toOption.get"
    )

    assert(errors.nonEmpty)
