"""Lesson 1: executable preconditions and postconditions."""

from .money import Balance, WithdrawalAmount


def withdraw(balance: Balance, amount: WithdrawalAmount) -> Balance:
    """Return the balance after withdrawing ``amount``.

    Raises:
        ValueError: if the amount exceeds the balance.
        AssertionError: if this implementation violates a postcondition.
    """
    if amount.value > balance.value:
        raise ValueError("amount must not exceed balance")

    new_balance = Balance(balance.value - amount.value)

    # These are implementation promises, not checks on the caller.
    if new_balance.value < 0:
        raise AssertionError("postcondition failed: result must be non-negative")
    if new_balance.value != balance.value - amount.value:
        raise AssertionError("postcondition failed: result must equal balance - amount")

    return new_balance
