"""Domain value types for the banking example."""

from dataclasses import dataclass


@dataclass(frozen=True, slots=True)
class Balance:
    value: int

    def __post_init__(self) -> None:
        if type(self.value) is not int:
            raise TypeError("balance must be an integer")
        if self.value < 0:
            raise ValueError("balance must be non-negative")


@dataclass(frozen=True, slots=True)
class WithdrawalAmount:
    value: int

    def __post_init__(self) -> None:
        if type(self.value) is not int:
            raise TypeError("withdrawal amount must be an integer")
        if self.value < 0:
            raise ValueError("withdrawal amount must be non-negative")
