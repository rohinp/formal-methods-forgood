import unittest
from pathlib import Path

from z3 import Solver, unsat


SPECIFICATION = (
    Path(__file__).resolve().parents[3] / "specs" / "withdraw_postcondition.smt2"
)


class WithdrawSmtTest(unittest.TestCase):
    def test_no_valid_withdrawal_violates_the_postconditions(self) -> None:
        solver = Solver()
        solver.from_file(str(SPECIFICATION))

        self.assertEqual(solver.check(), unsat)
