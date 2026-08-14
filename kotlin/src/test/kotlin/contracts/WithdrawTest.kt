package contracts

import banking.withdraw
import banking.Balance
import banking.WithdrawalAmount
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WithdrawTest {
    @Test
    fun `withdraws part of a balance`() {
        assertEquals(Balance(70), withdraw(Balance(100), WithdrawalAmount(30)))
    }

    @Test
    fun `may withdraw nothing`() {
        assertEquals(Balance(100), withdraw(Balance(100), WithdrawalAmount(0)))
    }

    @Test
    fun `may withdraw the entire balance`() {
        assertEquals(Balance(0), withdraw(Balance(100), WithdrawalAmount(100)))
    }

    @Test
    fun `rejects an overdraft`() {
        val error = assertFailsWith<IllegalArgumentException> {
            withdraw(Balance(100), WithdrawalAmount(101))
        }
        assertEquals("amount must not exceed balance", error.message)
    }
}
