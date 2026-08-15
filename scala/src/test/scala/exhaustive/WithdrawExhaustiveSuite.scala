package exhaustive

import banking.{Balance, WithdrawalAmount, Withdraw}
import banking.Withdraw.WithdrawalError.AmountExceedsBalance

class WithdrawExhaustiveSuite extends munit.FunSuite:
  private val MinValue = 0
  private val MaxValue = 20
  private val ExpectedCases = math.pow(MaxValue - MinValue + 1, 2).toInt

  test("every balance and amount in the bounded domain"):
    var evaluatedCases = 0

    for
      balanceValue <- MinValue to MaxValue
      amountValue <- MinValue to MaxValue
    do
      val balance = Balance.from(balanceValue).toOption.get
      val amount = WithdrawalAmount.from(amountValue).toOption.get
      val result = Withdraw.withdraw(balance, amount)

      if amountValue <= balanceValue then
        assertEquals(
          result.map(_.value),
          Right(balanceValue - amountValue),
          s"balance=$balanceValue, amount=$amountValue"
        )
      else
        assertEquals(
          result,
          Left(AmountExceedsBalance),
          s"balance=$balanceValue, amount=$amountValue"
        )

      evaluatedCases += 1

    assertEquals(evaluatedCases, ExpectedCases)
