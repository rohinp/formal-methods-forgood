package contracts

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WithdrawTest {
    @Test
    fun `withdraws part of a balance`() {
        assertEquals(70, withdraw(100, 30))
    }

    @Test
    fun `may withdraw nothing`() {
        assertEquals(100, withdraw(100, 0))
    }

    @Test
    fun `may withdraw the entire balance`() {
        assertEquals(0, withdraw(100, 100))
    }

    @Test
    fun `rejects a negative balance`() {
        val error = assertFailsWith<IllegalArgumentException> { withdraw(-1, 0) }
        assertEquals("balance must be non-negative", error.message)
    }

    @Test
    fun `rejects a negative amount`() {
        val error = assertFailsWith<IllegalArgumentException> { withdraw(100, -1) }
        assertEquals("amount must be non-negative", error.message)
    }

    @Test
    fun `rejects an overdraft`() {
        val error = assertFailsWith<IllegalArgumentException> { withdraw(100, 101) }
        assertEquals("amount must not exceed balance", error.message)
    }
}

