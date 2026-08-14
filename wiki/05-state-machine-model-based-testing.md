# 05 — State-machine and model-based testing

## Why a developer should care

A single withdrawal may work correctly while a sequence exposes a bug:

```text
start at 100 → withdraw 30 → reject 80 → withdraw 20 → withdraw 50
```

State-machine testing generates sequences like this and checks the system after
every step. Model-based testing supplies a small, independent description of
the expected behavior—the **reference model**—as the test oracle.

## Jargon

- **State** is the information remembered between operations. Our model keeps a
  balance and the successful withdrawal amounts.
- A **command** (or action) is an operation applied to a state. Here it is
  `Withdraw(amount)`.
- A **transition** describes the next state and result for a command.
- The **system under test (SUT)** is the real `BankAccount` implementation.
- A **reference model** is a smaller implementation used to predict behavior.
- A **trace** is one generated sequence of commands and observed states.
- A **postcondition** compares the model and SUT after each transition.

The state machine for this lesson has one command with two outcomes:

| Condition | Expected result | Next model state |
|---|---|---|
| `amount <= balance` | Accepted | Subtract amount and record it |
| `amount > balance` | Rejected | State unchanged |

The command is simple, but its result depends on all earlier commands. That is
what makes this stateful testing rather than a collection of independent cases.

## How the test works

Each language generates lists of 0–30 amounts between 0 and 150, starting from
a balance of 100. For every generated command, the test:

1. asks the model whether it should succeed and calculates the next model state;
2. executes the same withdrawal against the real account;
3. compares success or rejection;
4. compares the current balance and complete successful-withdrawal history.

Checking after **every** command identifies the first bad transition. Shrinking
from the property-testing library can then reduce a failing trace to a smaller
sequence.

## Why the model is separate

The production account uses domain types, invariant checks, encapsulation, and
language-specific error handling. The test model uses only an integer, a list,
and a tiny transition rule. Keeping it simpler reduces the chance that the test
copies the same defect as the implementation.

Do not implement a model by calling the production operation it is meant to
check. That creates two views of the same code, not an independent oracle.

## Language implementations

All three examples use the property-testing library already introduced in
lesson 2 to generate and shrink command traces. The model runner is written
explicitly so its four steps remain visible.

### Python

The mutable model and mutable account execute side by side. Exceptions are
translated into a boolean outcome before states are compared.

[Model-based test](../python/tests/statemachine/test_bank_account_model.py) ·
[Hypothesis stateful testing](https://hypothesis.readthedocs.io/en/latest/stateful.html)

Hypothesis also provides `RuleBasedStateMachine` for larger systems with many
commands and command preconditions. Our explicit runner is enough for this
one-command state machine and is easier to compare with the JVM examples.

### Scala 3

Both the reference model and production account are immutable. Each successful
transition returns the next value; a rejected transition retains the old one.

[Model-based test](../scala/src/test/scala/statemachine/BankAccountModelSuite.scala) ·
[ScalaCheck stateful-systems introduction](https://scalacheck.org/files/scaladays2014/) ·
[ScalaCheck documentation](https://scalacheck.org/documentation.html)

ScalaCheck also has a `Commands` abstraction for larger command-based models.
The explicit version here exposes the same model/command/transition mechanics
without adding framework ceremony to the first example.

### Kotlin

The reference model is immutable while the production account follows Kotlin's
mutable class style. `runCatching` converts the account's exception into an
outcome that can be compared with the model.

[Model-based test](../kotlin/src/test/kotlin/statemachine/BankAccountModelTest.kt) ·
[Kotest property testing](https://kotest.io/docs/proptest/property-based-testing.html) ·
[Kotest list generators](https://kotest.io/docs/proptest/property-test-generators-list.html)

## Static versus dynamic languages

This technique is primarily runtime verification in all three languages. Scala
and Kotlin type-check the model, commands, and SUT calls before execution;
Python relies on runtime behavior unless a separate type checker is used. None
of the three compilers proves that every possible trace agrees with the model.

## What this lesson guarantees

Passing means that all generated traces in that run agreed with the reference
model after every command. It gives confidence in ordering, repeated mutation,
failure atomicity, and interactions between earlier and later calls.

It is not exhaustive or a proof. This lesson has only one command, bounded
amounts, bounded trace lengths, and no concurrency. A wrong reference model can
also approve wrong production behavior. As the account gains deposits, fees,
or transfers, those must become new commands and model transitions.

## Run the examples

Use the same all-test commands from
[lesson 2](02-property-based-testing.md#run-the-examples).
