package statemachine

import banking.Balance
import banking.BankAccount
import banking.WithdrawalAmount
import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlin.test.assertEquals

private data class AccountModel(
    val balance: Int,
    val successfulWithdrawals: List<Int> = emptyList(),
) {
    fun withdraw(amount: Int): Pair<Boolean, AccountModel> =
        if (amount > balance) {
            false to this
        } else {
            true to copy(
                balance = balance - amount,
                successfulWithdrawals = successfulWithdrawals + amount,
            )
        }
}

class BankAccountModelTest : FunSpec({
    val commandSequences = Arb.list(Arb.int(0..150), 0..30)

    test("generated command sequences match the model") {
        checkAll(commandSequences) { commands ->
            var model = AccountModel(balance = 100)
            val account = BankAccount(Balance(100))

            commands.forEach { rawAmount ->
                val (expectedSuccess, nextModel) = model.withdraw(rawAmount)
                val actualSuccess = runCatching {
                    account.withdraw(WithdrawalAmount(rawAmount))
                }.isSuccess

                model = nextModel
                assertEquals(expectedSuccess, actualSuccess)
                assertEquals(model.balance, account.balance.value)
                assertEquals(
                    model.successfulWithdrawals,
                    account.withdrawals.map { it.value },
                )
            }
        }
    }
})
