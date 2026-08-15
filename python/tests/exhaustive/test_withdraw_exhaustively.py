import unittest

from banking import Balance, WithdrawalAmount, withdraw


MIN_VALUE = 0
MAX_VALUE = 20
EXPECTED_CASES = (MAX_VALUE - MIN_VALUE + 1) ** 2


class ExhaustiveWithdrawTest(unittest.TestCase):
    def test_every_balance_and_amount_in_the_bounded_domain(self) -> None:
        evaluated_cases = 0

        for balance_value in range(MIN_VALUE, MAX_VALUE + 1):
            for amount_value in range(MIN_VALUE, MAX_VALUE + 1):
                with self.subTest(balance=balance_value, amount=amount_value):
                    balance = Balance(balance_value)
                    amount = WithdrawalAmount(amount_value)

                    if amount_value <= balance_value:
                        self.assertEqual(
                            withdraw(balance, amount),
                            Balance(balance_value - amount_value),
                        )
                    else:
                        with self.assertRaises(ValueError):
                            withdraw(balance, amount)

                    evaluated_cases += 1

        self.assertEqual(evaluated_cases, EXPECTED_CASES)
