package propertytesting

import banking.Withdraw
import banking.Withdraw.WithdrawalError
import banking.{Balance, WithdrawalAmount}
import banking.Balance.*
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class WithdrawPropertiesSuite extends munit.ScalaCheckSuite:
  private val nonNegativeAmount = Gen.choose(0, 1_000_000)
  private val positiveAmount = Gen.choose(1, 1_000_000)

  property("a withdrawal preserves the balance equation"):
    forAll(nonNegativeAmount, nonNegativeAmount) { (remaining, amount) =>
      val balance = remaining + amount
      val validBalance = Balance.from(balance).toOption.get
      val validAmount = WithdrawalAmount.from(amount).toOption.get

      Withdraw.withdraw(validBalance, validAmount) match
        case Right(newBalance) =>
          assertEquals(newBalance.value, remaining)
          assertEquals(newBalance.value + amount, balance)
          assert(newBalance.value >= 0)
        case Left(error) => fail(s"valid withdrawal was rejected: $error")
    }

  property("an overdraft is always rejected"):
    forAll(nonNegativeAmount, positiveAmount) { (balance, extra) =>
      val validBalance = Balance.from(balance).toOption.get
      val overdraft = WithdrawalAmount.from(balance + extra).toOption.get

      assertEquals(
        Withdraw.withdraw(validBalance, overdraft),
        Left(WithdrawalError.AmountExceedsBalance)
      )
    }
