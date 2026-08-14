package invariants

import banking.Balance
import banking.BankAccount
import banking.WithdrawalAmount
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BankAccountTest {
    @Test
    fun `a new account starts in a consistent state`() {
        val account = BankAccount(Balance(100))

        assertEquals(100, account.balance.value)
        assertEquals(emptyList(), account.withdrawals)
        assertEquals(
            account.openingBalance.value,
            account.balance.value + account.withdrawals.sumOf { it.value },
        )
    }

    @Test
    fun `successful withdrawals preserve the invariant`() {
        val account = BankAccount(Balance(100))

        account.withdraw(WithdrawalAmount(25))
        account.withdraw(WithdrawalAmount(15))

        assertEquals(60, account.balance.value)
        assertEquals(listOf(25, 15), account.withdrawals.map { it.value })
        assertEquals(
            account.openingBalance.value,
            account.balance.value + account.withdrawals.sumOf { it.value },
        )
    }

    @Test
    fun `a failed withdrawal leaves state unchanged`() {
        val account = BankAccount(Balance(100))
        account.withdraw(WithdrawalAmount(25))

        assertFailsWith<IllegalArgumentException> {
            account.withdraw(WithdrawalAmount(80))
        }

        assertEquals(75, account.balance.value)
        assertEquals(listOf(25), account.withdrawals.map { it.value })
    }

    @Test
    fun `a previously returned history is a snapshot`() {
        val account = BankAccount(Balance(100))
        val observedHistory = account.withdrawals

        account.withdraw(WithdrawalAmount(10))

        assertEquals(emptyList(), observedHistory)
        assertEquals(listOf(10), account.withdrawals.map { it.value })
    }
}
