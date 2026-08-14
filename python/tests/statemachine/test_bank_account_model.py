import unittest
from dataclasses import dataclass, field

from hypothesis import given
from hypothesis import strategies as st

from banking import Balance, BankAccount, WithdrawalAmount


@dataclass
class AccountModel:
    balance: int
    successful_withdrawals: list[int] = field(default_factory=list)

    def withdraw(self, amount: int) -> bool:
        if amount > self.balance:
            return False

        self.balance -= amount
        self.successful_withdrawals.append(amount)
        return True


command_sequences = st.lists(
    st.integers(min_value=0, max_value=150),
    min_size=0,
    max_size=30,
)


class BankAccountModelTest(unittest.TestCase):
    @given(commands=command_sequences)
    def test_generated_command_sequences_match_the_model(
        self, commands: list[int]
    ) -> None:
        model = AccountModel(balance=100)
        account = BankAccount(Balance(100))

        for raw_amount in commands:
            expected_success = model.withdraw(raw_amount)

            try:
                account.withdraw(WithdrawalAmount(raw_amount))
                actual_success = True
            except ValueError:
                actual_success = False

            self.assertEqual(actual_success, expected_success)
            self.assertEqual(account.balance.value, model.balance)
            self.assertEqual(
                [amount.value for amount in account.withdrawals],
                model.successful_withdrawals,
            )
