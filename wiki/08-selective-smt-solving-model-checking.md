# 08 — Selective SMT solving and model checking

## Why a developer should care

Most application code does not need a theorem prover. A small amount of
critical arithmetic or a compact stateful protocol sometimes does.

This lesson applies two focused techniques:

1. an SMT solver searches for a valid withdrawal that violates its contract;
2. an explicit-state model checker explores every reachable state of a tiny
   bounded account model.

“Selective” is important. We isolate logic that is small enough to specify,
analyze, and review rather than attempting to formalize the entire application.

## Jargon

- **SAT** means a formula is satisfiable: at least one assignment makes it true.
- **UNSAT** means no assignment satisfies the formula.
- **SMT** means satisfiability modulo theories: SAT-style solving combined with
  theories such as integers, bit-vectors, arrays, or strings.
- A **model** in SMT terminology is a satisfying assignment. When we ask for a
  contract violation, that model is a counterexample.
- A **transition system** consists of states, an initial state, and rules for
  moving between states.
- A **reachable state** can be produced from the initial state by zero or more
  transitions.
- A **safety property** says that something bad never occurs. Our account
  invariant is a safety property.
- A **fixed point** is reached when exploring transitions discovers no new
  states.

## Part 1: prove the arithmetic formula with SMT

The shared [`withdraw_postcondition.smt2`](../specs/withdraw_postcondition.smt2)
uses the standard SMT-LIB language and the `QF_LIA` logic: quantifier-free
linear integer arithmetic.

It defines withdrawal as subtraction, assumes the three preconditions, and then
asserts that at least one postcondition is false. In other words, it asks the
solver to find a counterexample:

```text
balance >= 0
amount >= 0
amount <= balance
result = balance - amount

find: result < 0 OR result != balance - amount
```

Z3 returns `unsat`. Because the violation formula has no satisfying assignment,
the postconditions follow from the preconditions and the defined calculation.

[Z3](https://github.com/z3prover/z3/wiki) ·
[Programming Z3](https://z3prover.github.io/papers/programmingz3.html) ·
[SMT-LIB logics](https://smt-lib.org/logics.shtml)

### Executing the proof

Python's test suite loads the shared SMT-LIB file through the Z3 binding and
asserts that the answer is `unsat`:

[SMT test](../python/tests/smt/test_withdraw_smt.py)

```sh
cd python
PYTHONPATH=src python -m unittest tests.smt.test_withdraw_smt -v
```

The specification is language-neutral. It describes the arithmetic used by
Python, Scala, and Kotlin; the Python binding is simply the lightest way to run
the shared solver query in this repository. Adding separate native Z3 bindings
to both JVM builds would duplicate infrastructure without strengthening the
formula.

### Mathematical integers versus machine integers

SMT-LIB `Int` values are mathematical integers with no overflow. That directly
matches Python integers for this example. Scala and Kotlin use fixed-width
`Int`, but the withdrawal preconditions imply:

```text
0 <= balance - amount <= balance
```

so this particular subtraction cannot overflow. For arithmetic where overflow
is possible, model JVM integers with 32-bit SMT **bit-vectors**, or prove
separate range conditions before using mathematical integers.

## Part 2: explore a finite state model

The model checker starts from:

```text
(balance = 3, withdrawn = 0)
```

At every state it tries withdrawal amounts `0..3`. A valid withdrawal moves
value from `balance` to `withdrawn`; an overdraft leaves the model unchanged.
Every reached state must satisfy:

```text
balance >= 0
balance + withdrawn == 3
```

The checker stores visited states and explores each new state once. It reaches
exactly:

```text
(3, 0), (2, 1), (1, 2), (0, 3)
```

There are infinitely many command traces because zero withdrawals and rejected
overdrafts form loops. Nevertheless, these traces can only revisit the same four
states. Checking all four commands from all four states—16 transitions—reaches
a fixed point and covers every future state in this bounded abstraction.

The implementations retain the command trace used to reach each state so an
invariant failure reports a small counterexample path.

- [Python model checker](../python/tests/modelchecking/test_account_model_checker.py)
- [Scala model checker](../scala/src/test/scala/modelchecking/AccountModelCheckerSuite.scala)
- [Kotlin model checker](../kotlin/src/test/kotlin/modelchecking/AccountModelCheckerTest.kt)

This is the core algorithm used by an **explicit-state** model checker. Mature
tools such as TLC add specification languages, symmetry reduction, liveness
checking, distributed exploration, and better diagnostics.

[TLA+ tools](https://lamport.org/tla/tools.html) ·
[TLA+ invariant definition](https://tla.msr-inria.fr/tlatoolbox/doc/model/overview-page.html)

## How this differs from earlier lessons

| Technique | What is explored | What is checked |
|---|---|---|
| Property-based testing | Generated concrete inputs | Production behavior |
| State-machine testing | Generated command traces | Production versus reference model |
| Bounded exhaustive testing | Every input in a declared product | Production behavior |
| SMT solving | Logical assignments, often without enumeration | Formula satisfiability or validity |
| Model checking | Every reachable state in a bounded transition system | Model safety properties |

The techniques complement rather than replace one another. SMT and model
checking can make stronger claims about their specifications, while executable
tests connect those specifications to production code.

## The model-to-code gap

The explicit-state checker proves the invariant for its small abstract model,
not directly for `BankAccount`. A production implementation may still differ
from the transition rule. Lesson 5 reduces this gap by comparing generated
production traces with a reference model, while contracts and tests check the
actual implementation.

Similarly, `unsat` proves the SMT formula, not that the formula expresses the
right business requirement. Specifications need review just as code does.

## What this lesson guarantees

- Z3 found no counterexample to the withdrawal postconditions in mathematical
  integer arithmetic under the stated preconditions.
- All reachable states of the bounded account model were explored.
- The conservation and non-negative-balance invariants held in all four states
  and across the explored transition graph.

It does not prove the whole application, concurrency behavior, external
systems, serialization, or unmodeled operations. Larger bounds and richer state
can also cause state-space explosion. Solvers may return `unknown` for theories
or problems they cannot decide within available resources.

## Run the examples

Install the current Python development dependencies, which include the Z3
binding:

```sh
cd python
pip install -r requirements-dev.txt
```

Then use the all-test commands from
[lesson 2](02-property-based-testing.md#run-the-examples). The Python suite runs
the SMT query; all three suites run their model checker.
