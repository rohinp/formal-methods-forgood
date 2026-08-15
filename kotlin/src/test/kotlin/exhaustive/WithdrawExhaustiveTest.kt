package exhaustive

import banking.Balance
import banking.WithdrawalAmount
import banking.withdraw
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WithdrawExhaustiveTest {
    @Test
    fun `every balance and amount in the bounded domain`() {
        val boundedDomain = 0..20
        val expectedCases = boundedDomain.count() * boundedDomain.count()
        var evaluatedCases = 0

        for (balanceValue in boundedDomain) {
            for (amountValue in boundedDomain) {
                val balance = Balance(balanceValue)
                val amount = WithdrawalAmount(amountValue)
                val caseDescription = "balance=$balanceValue, amount=$amountValue"

                if (amountValue <= balanceValue) {
                    assertEquals(
                        Balance(balanceValue - amountValue),
                        withdraw(balance, amount),
                        caseDescription,
                    )
                } else {
                    assertFailsWith<IllegalArgumentException>(caseDescription) {
                        withdraw(balance, amount)
                    }
                }

                evaluatedCases += 1
            }
        }

        assertEquals(expectedCases, evaluatedCases)
    }
}
