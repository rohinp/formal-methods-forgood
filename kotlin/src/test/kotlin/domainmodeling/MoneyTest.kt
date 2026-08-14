package domainmodeling

import banking.Balance
import banking.WithdrawalAmount
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MoneyTest {
    @Test
    fun `balance rejects negative values`() {
        val error = assertFailsWith<IllegalArgumentException> { Balance(-1) }
        assertEquals("balance must be non-negative", error.message)
    }

    @Test
    fun `withdrawal amount rejects negative values`() {
        val error = assertFailsWith<IllegalArgumentException> { WithdrawalAmount(-1) }
        assertEquals("withdrawal amount must be non-negative", error.message)
    }
}

