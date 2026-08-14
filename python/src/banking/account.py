"""A bank account whose public operations preserve a class invariant."""

from .money import Balance, WithdrawalAmount
from .withdraw import withdraw


class BankAccount:
    """Track withdrawals while keeping balance and history consistent."""

    def __init__(self, opening_balance: Balance) -> None:
        self._opening_balance = opening_balance
        self._balance = opening_balance
        self._withdrawals: list[WithdrawalAmount] = []
        self._assert_invariant(self._balance, self._withdrawals)

    @property
    def opening_balance(self) -> Balance:
        return self._opening_balance

    @property
    def balance(self) -> Balance:
        return self._balance

    @property
    def withdrawals(self) -> tuple[WithdrawalAmount, ...]:
        return tuple(self._withdrawals)

    def withdraw(self, amount: WithdrawalAmount) -> None:
        candidate_balance = withdraw(self._balance, amount)
        candidate_withdrawals = [*self._withdrawals, amount]

        # Validate the whole candidate state before changing this object.
        self._assert_invariant(candidate_balance, candidate_withdrawals)
        self._balance = candidate_balance
        self._withdrawals.append(amount)

    def _assert_invariant(
        self,
        balance: Balance,
        withdrawals: list[WithdrawalAmount],
    ) -> None:
        withdrawn_total = sum(amount.value for amount in withdrawals)
        if balance.value + withdrawn_total != self._opening_balance.value:
            raise AssertionError(
                "account invariant failed: balance plus withdrawals "
                "must equal opening balance"
            )
