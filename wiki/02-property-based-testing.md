# 02 — Property-based testing

## Why a developer should care

An example-based test checks values chosen by a developer:

```text
withdraw(100, 30) == 70
```

A **property-based test** describes a rule and asks a library to generate many
inputs that should obey it:

```text
new balance + withdrawn amount == original balance
```

This is valuable for AI-generated code because the test describes behavior that
must remain true without trying to predict every implementation or edge case.

## Jargon

- A **property** is a rule expected to hold across a set of inputs.
- A **generator** produces inputs from a defined domain.
- A **counterexample** is an input that makes the property fail.
- **Shrinking** searches from a failing input toward a smaller, simpler
  counterexample that is easier to understand.
- The **test oracle** decides whether an output is correct. Here, the balance
  equation acts as our oracle.

## The properties in this repository

We generate `remaining` and `amount` as non-negative integers, then construct:

```text
balance = remaining + amount
```

This creates valid input **by construction**, so the test does not waste cases
by generating invalid combinations and discarding them. We check that:

```text
withdraw(balance, amount) == remaining
new balance + amount == balance
new balance >= 0
```

A second property generates a positive `extra` amount and verifies that every
`withdraw(balance, balance + extra)` is rejected.

The generators stop at `1,000,000`. Python integers can grow beyond this, but
Scala and Kotlin `Int` values have fixed bounds. A deliberately safe common
range keeps this lesson about property testing rather than integer overflow.

## Language choices

### Python: Hypothesis

Hypothesis integrates with `unittest`, generates inputs from strategies, stores
useful failing examples, and shrinks failures.

[Property tests](../python/tests/propertytesting/test_withdraw_properties.py) ·
[Hypothesis documentation](https://hypothesis.readthedocs.io/) ·
[Strategies](https://hypothesis.readthedocs.io/en/latest/reference/strategies.html)

### Scala 3: ScalaCheck with MUnit

ScalaCheck supplies generators and shrinking. The MUnit integration lets the
properties use the same assertions and test runner as lesson 1.

[Property tests](../scala/src/test/scala/propertytesting/WithdrawPropertiesSuite.scala) ·
[MUnit ScalaCheck integration](https://scalameta.org/munit/docs/integrations/scalacheck.html) ·
[ScalaCheck documentation](https://scalacheck.org/documentation.html)

### Kotlin: Kotest Property

Kotest Property provides Kotlin generators (`Arb`) and `checkAll`. It runs here
through Kotest's JUnit Platform runner, alongside the existing Kotlin tests.

[Property tests](../kotlin/src/test/kotlin/propertytesting/WithdrawPropertyTest.kt) ·
[Kotest property testing](https://kotest.io/docs/proptest/property-based-testing.html) ·
[Property test functions](https://kotest.io/docs/proptest/property-test-functions.html)

## Run the examples

Enter the Python project, create a virtual environment, and install the test
dependency once:

```sh
cd python
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements-dev.txt
```

Then run the Python property tests:

```sh
PYTHONPATH=src python -m unittest discover -s tests -p 'test_*.py' -v
```

Run all Scala tests:

```sh
cd scala
sbt test
```

Run all Kotlin tests:

```sh
cd kotlin
./gradlew test --rerun
```

## What this lesson guarantees

Passing means the properties held for every generated case in that run. It
provides broader confidence than a few examples, but it is **not a proof** that
the properties hold for every possible input. It is also only as strong as the
properties and generators we write.

If a property fails, keep the reported counterexample or reproduction seed.
That turns a randomly discovered failure into a repeatable regression test.
