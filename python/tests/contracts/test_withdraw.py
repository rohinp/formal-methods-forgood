import unittest

from banking import Balance, WithdrawalAmount, withdraw


class WithdrawContractTest(unittest.TestCase):
    def test_withdraws_part_of_balance(self) -> None:
        self.assertEqual(withdraw(Balance(100), WithdrawalAmount(30)), Balance(70))

    def test_may_withdraw_nothing(self) -> None:
        self.assertEqual(withdraw(Balance(100), WithdrawalAmount(0)), Balance(100))

    def test_may_withdraw_entire_balance(self) -> None:
        self.assertEqual(withdraw(Balance(100), WithdrawalAmount(100)), Balance(0))

    def test_rejects_overdraft(self) -> None:
        with self.assertRaisesRegex(ValueError, "amount must not exceed balance"):
            withdraw(Balance(100), WithdrawalAmount(101))


if __name__ == "__main__":
    unittest.main()
