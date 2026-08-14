# 01 — Contracts

## Why a developer should care

A function usually has undocumented assumptions about its inputs and promises
about its output. A **contract** makes those rules explicit and executable.
When an AI changes the implementation, the contract provides a stable boundary
against which that change can be checked.

Our example is:

```text
withdraw(balance, amount) -> new balance

Requires: balance >= 0, amount >= 0, amount <= balance
Ensures:  result >= 0, result == balance - amount
```

## Jargon

- A **precondition** is a predicate that must be true before an operation runs.
- A **postcondition** is a predicate the implementation guarantees afterward.
- A **predicate** is simply an expression that evaluates to true or false.
- A **contract violation** means caller or implementation code broke an agreed
  rule. It is different from a normal business outcome such as a declined card.

The caller is responsible for preconditions. The function is responsible for
postconditions. Runtime checks detect violations only on paths that actually
execute; they do not prove the rule for every possible input.

After [lesson 3](03-domain-modeling-with-types.md), non-negativity is enforced
when `Balance` and `WithdrawalAmount` values are constructed. The remaining
operation-level precondition is `amount <= balance`.

## Language choices

### Python

Python conventionally reports an overdraft with `ValueError`. It raises
`AssertionError` if its own postcondition fails. The domain wrappers validate
non-negativity when constructed.

[Implementation](../python/src/banking/withdraw.py) ·
[Tests](../python/tests/contracts/test_withdraw.py) ·
[Python exceptions](https://docs.python.org/3/tutorial/errors.html)

### Scala 3

The Scala example returns `Either[WithdrawalError, Balance]`. An overdraft is
therefore visible in the return type and can be composed with `map`, `flatMap`,
or a `for` expression without throwing. The postcondition uses `ensuring`: if
it fails, the implementation itself is defective, so an assertion failure is
appropriate.

[Implementation](../scala/src/main/scala/banking/Withdraw.scala) ·
[Tests](../scala/src/test/scala/contracts/WithdrawSuite.scala) ·
[Scala functional error handling](https://docs.scala-lang.org/scala3/book/fp-functional-error-handling.html) ·
[Scala postconditions](https://docs.scala-lang.org/scala3/reference/contextual/context-functions.html)

### Kotlin

Kotlin's standard `require` checks the overdraft precondition and `check` checks
postconditions. The domain value classes validate non-negativity during
construction. Kotlin's `Result` can be useful at API boundaries that capture
expected failures, but wrapping every programmer mistake in `Result` would make
this small example less idiomatic.

[Implementation](../kotlin/src/main/kotlin/banking/Withdraw.kt) ·
[Tests](../kotlin/src/test/kotlin/contracts/WithdrawTest.kt) ·
[Kotlin preconditions](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/require.html) ·
[Kotlin state checks](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/check.html) ·
[Kotlin contracts and data-flow analysis](https://kotlinlang.org/spec/control--and-data-flow-analysis.html)

## Run the examples

From the Python project directory, run all Python tests (after the one-time
dependency setup in [lesson 2](02-property-based-testing.md#run-the-examples)):

```sh
cd python
PYTHONPATH=src python -m unittest discover -s tests -p 'test_*.py' -v
```

Run the Scala tests:

```sh
cd scala
sbt test
```

Run the Kotlin tests:

```sh
cd kotlin
./gradlew test --rerun
```

## What this lesson guarantees

For every call that completes successfully, the checked postconditions held for
that execution. It does **not** yet guarantee that all possible valid inputs
work. Property-based testing is our next step toward broader confidence.

## Libraries we may use later

There are useful libraries, but each belongs to a specific lesson:

- Python: [Hypothesis](https://hypothesis.readthedocs.io/) for generated tests,
  [icontract](https://icontract.readthedocs.io/) for richer contracts, and
  [CrossHair](https://crosshair.readthedocs.io/) for symbolic checking.
- Scala: [ScalaCheck](https://scalacheck.org/) for generated tests, refined-type
  libraries such as [Iron](https://iltotore.github.io/iron/docs/), and
  [Stainless](https://github.com/epfl-lara/stainless) for deeper verification.
- Kotlin: [Kotest property testing](https://kotest.io/docs/proptest/property-based-testing.html)
  for generated tests. [Arrow typed errors](https://arrow-kt.io/learn/typed-errors/)
  is useful when a project adopts functional error handling, but it is not
  necessary for this small contract example.

We will introduce these only when their technique becomes the active lesson.
