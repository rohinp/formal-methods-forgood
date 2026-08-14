import unittest
from dataclasses import FrozenInstanceError

from banking import Balance, WithdrawalAmount


class MoneyDomainTypesTest(unittest.TestCase):
    def test_balance_rejects_negative_values(self) -> None:
        with self.assertRaisesRegex(ValueError, "balance must be non-negative"):
            Balance(-1)

    def test_withdrawal_amount_rejects_negative_values(self) -> None:
        with self.assertRaisesRegex(ValueError, "withdrawal amount must be non-negative"):
            WithdrawalAmount(-1)

    def test_runtime_construction_rejects_the_wrong_primitive_type(self) -> None:
        with self.assertRaisesRegex(TypeError, "balance must be an integer"):
            Balance(1.5)  # type: ignore[arg-type]

    def test_values_cannot_be_changed_normally_after_construction(self) -> None:
        balance = Balance(100)

        with self.assertRaises(FrozenInstanceError):
            balance.value = 50  # type: ignore[misc]
