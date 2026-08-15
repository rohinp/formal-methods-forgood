"""Small pure functions suitable for symbolic analysis."""


def calculate_new_balance(balance: int, amount: int) -> int:
    """Calculate a valid post-withdrawal balance.

    pre: balance >= 0
    pre: amount >= 0
    pre: amount <= balance
    post: __return__ >= 0
    post: __return__ == balance - amount
    """
    return balance - amount
