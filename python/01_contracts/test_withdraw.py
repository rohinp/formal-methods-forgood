import unittest

from withdraw import withdraw


class WithdrawContractTest(unittest.TestCase):
    def test_withdraws_part_of_balance(self) -> None:
        self.assertEqual(withdraw(100, 30), 70)

    def test_may_withdraw_nothing(self) -> None:
        self.assertEqual(withdraw(100, 0), 100)

    def test_may_withdraw_entire_balance(self) -> None:
        self.assertEqual(withdraw(100, 100), 0)

    def test_rejects_negative_balance(self) -> None:
        with self.assertRaisesRegex(ValueError, "balance must be non-negative"):
            withdraw(-1, 0)

    def test_rejects_negative_amount(self) -> None:
        with self.assertRaisesRegex(ValueError, "amount must be non-negative"):
            withdraw(100, -1)

    def test_rejects_overdraft(self) -> None:
        with self.assertRaisesRegex(ValueError, "amount must not exceed balance"):
            withdraw(100, 101)


if __name__ == "__main__":
    unittest.main()

