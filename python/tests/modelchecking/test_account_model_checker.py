import unittest
from collections import deque
from dataclasses import dataclass


OPENING_BALANCE = 3
COMMANDS = range(0, OPENING_BALANCE + 1)


@dataclass(frozen=True)
class ModelState:
    balance: int
    withdrawn: int


@dataclass(frozen=True)
class Trace:
    state: ModelState
    commands: tuple[int, ...]


def transition(state: ModelState, amount: int) -> ModelState:
    if amount > state.balance:
        return state
    return ModelState(
        balance=state.balance - amount,
        withdrawn=state.withdrawn + amount,
    )


def explore_reachable_states() -> tuple[set[ModelState], int]:
    initial = ModelState(balance=OPENING_BALANCE, withdrawn=0)
    visited = {initial}
    frontier = deque([Trace(initial, ())])
    evaluated_transitions = 0

    while frontier:
        current = frontier.popleft()
        state = current.state
        assert state.balance >= 0, f"negative balance after {current.commands}"
        assert state.balance + state.withdrawn == OPENING_BALANCE, (
            f"conservation invariant failed after {current.commands}"
        )

        for amount in COMMANDS:
            evaluated_transitions += 1
            next_state = transition(state, amount)
            if next_state not in visited:
                visited.add(next_state)
                frontier.append(Trace(next_state, (*current.commands, amount)))

    return visited, evaluated_transitions


class AccountModelCheckerTest(unittest.TestCase):
    def test_every_reachable_state_preserves_the_invariant(self) -> None:
        states, evaluated_transitions = explore_reachable_states()

        self.assertEqual(
            states,
            {
                ModelState(3, 0),
                ModelState(2, 1),
                ModelState(1, 2),
                ModelState(0, 3),
            },
        )
        self.assertEqual(evaluated_transitions, 16)
