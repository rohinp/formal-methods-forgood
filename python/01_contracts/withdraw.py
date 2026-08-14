"""Lesson 1: executable preconditions and postconditions."""


def withdraw(balance: int, amount: int) -> int:
    """Return the balance after withdrawing ``amount``.

    Raises:
        ValueError: if a precondition is violated.
        AssertionError: if this implementation violates a postcondition.

    Type hints document the intended input type. Unlike Scala's types, Python
    does not enforce these hints at runtime.
    """
    if balance < 0:
        raise ValueError("balance must be non-negative")
    if amount < 0:
        raise ValueError("amount must be non-negative")
    if amount > balance:
        raise ValueError("amount must not exceed balance")

    new_balance = balance - amount

    # These are implementation promises, not checks on the caller.
    if new_balance < 0:
        raise AssertionError("postcondition failed: result must be non-negative")
    if new_balance != balance - amount:
        raise AssertionError("postcondition failed: result must equal balance - amount")

    return new_balance

