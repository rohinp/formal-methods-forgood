import unittest

from hypothesis import given
from hypothesis import strategies as st

from banking.withdraw import withdraw


non_negative_amounts = st.integers(min_value=0, max_value=1_000_000)
positive_amounts = st.integers(min_value=1, max_value=1_000_000)


class WithdrawPropertiesTest(unittest.TestCase):
    @given(remaining=non_negative_amounts, amount=non_negative_amounts)
    def test_withdrawal_preserves_the_balance_equation(
        self, remaining: int, amount: int
    ) -> None:
        balance = remaining + amount

        new_balance = withdraw(balance, amount)

        self.assertEqual(new_balance, remaining)
        self.assertEqual(new_balance + amount, balance)
        self.assertGreaterEqual(new_balance, 0)

    @given(balance=non_negative_amounts, extra=positive_amounts)
    def test_overdraft_is_always_rejected(self, balance: int, extra: int) -> None:
        with self.assertRaises(ValueError):
            withdraw(balance, balance + extra)


if __name__ == "__main__":
    unittest.main()
