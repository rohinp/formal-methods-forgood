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

## Language choices

### Python

Python conventionally reports invalid arguments with exceptions, so the example
raises `ValueError`. It raises `AssertionError` if its own postcondition fails.
Type hints describe the intended types but do not enforce them at runtime.

[Implementation](../python/01_contracts/withdraw.py) ·
[Tests](../python/01_contracts/test_withdraw.py) ·
[Python exceptions](https://docs.python.org/3/tutorial/errors.html)

### Scala 3

The Scala example returns `Either[WithdrawalError, Int]`. Invalid input is
therefore visible in the return type and can be composed with `map`, `flatMap`,
or a `for` expression without throwing. The postcondition uses `ensuring`: if it
fails, the implementation itself is defective, so an assertion failure is
appropriate.

[Implementation](../scala/01_contracts/src/main/scala/Withdraw.scala) ·
[Tests](../scala/01_contracts/src/test/scala/WithdrawSuite.scala) ·
[Scala functional error handling](https://docs.scala-lang.org/scala3/book/fp-functional-error-handling.html) ·
[Scala postconditions](https://docs.scala-lang.org/scala3/reference/contextual/context-functions.html)

### Kotlin

Kotlin's standard `require` is the conventional check for invalid function
arguments and `check` is used for invalid program state. They also help the
compiler's data-flow analysis. Kotlin's `Result` is useful at API boundaries
that intentionally capture failures, but wrapping every programmer mistake in
`Result` would make the example less idiomatic.

[Implementation](../kotlin/src/main/kotlin/contracts/Withdraw.kt) ·
[Tests](../kotlin/src/test/kotlin/contracts/WithdrawTest.kt) ·
[Kotlin preconditions](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/require.html) ·
[Kotlin state checks](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/check.html) ·
[Kotlin contracts and data-flow analysis](https://kotlinlang.org/spec/control--and-data-flow-analysis.html)

## Run the examples

From the repository root, run the Python tests:

```sh
python3 -m unittest discover -s python/01_contracts -p 'test_*.py' -v
```

Run the Scala tests:

```sh
sbt test
```

Run the Kotlin tests:

```sh
cd kotlin
./gradlew test
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
