package propertytesting

import banking.Withdraw
import banking.Withdraw.WithdrawalError
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class WithdrawPropertiesSuite extends munit.ScalaCheckSuite:
  private val nonNegativeAmount = Gen.choose(0, 1_000_000)
  private val positiveAmount = Gen.choose(1, 1_000_000)

  property("a withdrawal preserves the balance equation"):
    forAll(nonNegativeAmount, nonNegativeAmount) { (remaining, amount) =>
      val balance = remaining + amount

      Withdraw.withdraw(balance, amount) match
        case Right(newBalance) =>
          assertEquals(newBalance, remaining)
          assertEquals(newBalance + amount, balance)
          assert(newBalance >= 0)
        case Left(error) => fail(s"valid withdrawal was rejected: $error")
    }

  property("an overdraft is always rejected"):
    forAll(nonNegativeAmount, positiveAmount) { (balance, extra) =>
      assertEquals(
        Withdraw.withdraw(balance, balance + extra),
        Left(WithdrawalError.AmountExceedsBalance)
      )
    }
