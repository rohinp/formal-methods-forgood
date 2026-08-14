package propertytesting

import banking.withdraw
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class WithdrawPropertyTest : FunSpec({
    val nonNegativeAmount = Arb.int(0..1_000_000)
    val positiveAmount = Arb.int(1..1_000_000)

    test("a withdrawal preserves the balance equation") {
        checkAll(nonNegativeAmount, nonNegativeAmount) { remaining, amount ->
            val balance = remaining + amount

            val newBalance = withdraw(balance, amount)

            assertEquals(remaining, newBalance)
            assertEquals(balance, newBalance + amount)
            assertTrue(newBalance >= 0)
        }
    }

    test("an overdraft is always rejected") {
        checkAll(nonNegativeAmount, positiveAmount) { balance, extra ->
            assertFailsWith<IllegalArgumentException> {
                withdraw(balance, balance + extra)
            }
        }
    }
})
