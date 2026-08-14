import unittest

from banking import Balance, BankAccount, WithdrawalAmount


class BankAccountInvariantTest(unittest.TestCase):
    def test_new_account_starts_in_a_consistent_state(self) -> None:
        account = BankAccount(Balance(100))

        self.assertEqual(account.balance, Balance(100))
        self.assertEqual(account.withdrawals, ())
        self.assertEqual(
            account.balance.value + sum(a.value for a in account.withdrawals),
            account.opening_balance.value,
        )

    def test_successful_withdrawals_preserve_the_invariant(self) -> None:
        account = BankAccount(Balance(100))

        account.withdraw(WithdrawalAmount(25))
        account.withdraw(WithdrawalAmount(15))

        self.assertEqual(account.balance, Balance(60))
        self.assertEqual(
            account.withdrawals,
            (WithdrawalAmount(25), WithdrawalAmount(15)),
        )
        self.assertEqual(
            account.balance.value + sum(a.value for a in account.withdrawals),
            account.opening_balance.value,
        )

    def test_failed_withdrawal_leaves_state_unchanged(self) -> None:
        account = BankAccount(Balance(100))
        account.withdraw(WithdrawalAmount(25))

        with self.assertRaisesRegex(ValueError, "amount must not exceed balance"):
            account.withdraw(WithdrawalAmount(80))

        self.assertEqual(account.balance, Balance(75))
        self.assertEqual(account.withdrawals, (WithdrawalAmount(25),))

    def test_withdrawal_history_does_not_expose_the_mutable_list(self) -> None:
        account = BankAccount(Balance(100))
        observed_history = account.withdrawals

        account.withdraw(WithdrawalAmount(10))

        self.assertEqual(observed_history, ())
        self.assertEqual(account.withdrawals, (WithdrawalAmount(10),))
