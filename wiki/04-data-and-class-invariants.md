# 04 — Data and class invariants

## Why a developer should care

A `Balance` can guarantee one local rule: its value is non-negative. A bank
account has a stronger rule involving several pieces of state:

```text
current balance + total successful withdrawals == opening balance
```

This is an **invariant**: a condition that must remain true for every stable,
externally visible state of the account.

The account establishes the rule when it is created and preserves it after
every public operation. A failed withdrawal must preserve it too by leaving the
account unchanged.

## Jargon

- A **data invariant** is a rule that valid data must always satisfy.
- A **class invariant** applies that idea to every stable instance of a class.
- A **representation invariant** describes which combinations of internal
  fields represent a valid object.
- To **establish** an invariant is to make it true during construction.
- To **preserve** an invariant is to ensure each operation leaves it true.
- **Encapsulation** limits the ways callers can alter internal state, making
  preservation manageable.

“Always” means at the object's public boundaries: after construction and before
and after public calls. A method may temporarily hold intermediate values, but
it must not expose or commit an invalid state. This boundary interpretation is
also how class invariants are described in
[Eiffel's Design by Contract documentation](https://www.eiffel.org/doc/eiffel/I2E-_Design_by_Contract_and_Assertions).

## The implementation pattern

Our account stores an opening balance, current balance, and withdrawal history.
Its withdrawal operation follows three steps:

1. calculate a complete candidate state;
2. check the invariant on that candidate;
3. only then publish or return the state.

This order matters for mutable objects: if validation fails, no partial update
has escaped.

## Python: convention-backed encapsulation

Python uses a mutable `BankAccount`. Underscore-prefixed fields communicate that
they are internal, properties expose read-only views, and withdrawal history is
returned as a tuple. The method checks a candidate balance and history before
committing either change.

[Account](../python/src/banking/account.py) ·
[Tests](../python/tests/invariants/test_bank_account.py) ·
[Python classes and private-name conventions](https://docs.python.org/3/tutorial/classes.html#private-variables)

Python has no truly private instance variables. Callers can deliberately alter
underscore-prefixed fields, and annotations do not prove the invariant. The
runtime check protects construction and the public methods we wrote; it cannot
cover code paths that bypass them. Returning immutable snapshots prevents
ordinary accidental mutation, not hostile reflection or deliberate internals
access.

## Scala 3: immutable state transitions

Scala's account has immutable `val` fields and a private constructor. `open`
creates the initial valid value, and `withdraw` returns an `Either` containing
either the domain error or a newly validated account. The original value never
changes.

[Account](../scala/src/main/scala/banking/BankAccount.scala) ·
[Tests](../scala/src/test/scala/invariants/BankAccountSuite.scala) ·
[Scala classes](https://docs.scala-lang.org/tour/classes.html) ·
[Scala immutable fields](https://docs.scala-lang.org/scala3/book/domain-modeling-tools.html)

Immutability and the private constructor sharply reduce the places that must
preserve the invariant. The compiler enforces access and assignment rules, but
it does not prove our arithmetic equation. That relationship is still checked
at runtime when an account is created.

## Kotlin: private mutation and snapshots

Kotlin uses the common mutable-object style: `balance` has a private setter, the
mutable history is private, and callers receive a copied `List`. As in Python,
the candidate state is checked before fields are changed.

[Account](../kotlin/src/main/kotlin/banking/BankAccount.kt) ·
[Tests](../kotlin/src/test/kotlin/invariants/BankAccountTest.kt) ·
[Kotlin properties and private setters](https://kotlinlang.org/docs/properties.html) ·
[Kotlin visibility modifiers](https://kotlinlang.org/docs/visibility-modifiers.html)

The Kotlin compiler prevents ordinary callers from assigning the balance or
accessing the mutable list. It does not prove the cross-field equation, and
reflection, Java interop, or some serializers may bypass normal access paths.
The invariant check therefore remains a runtime guarantee.

## What this lesson guarantees

| Guarantee | Python | Scala 3 | Kotlin |
|---|---|---|---|
| Construction establishes the invariant | Runtime | Runtime | Runtime |
| Public withdrawal preserves it | Runtime | Runtime | Runtime |
| Ordinary callers can directly replace state | Convention discourages it | No | No |
| Compiler proves the equation | No | No | No |
| Failed withdrawal leaves the account unchanged | Tested | By immutability | Tested |

This remains executable checking, not a mathematical proof of every possible
execution. In particular, fixed-width integer overflow can break arithmetic
reasoning in all three examples and is intentionally deferred. The next lesson
will generate and verify longer sequences of account operations with a state
machine model.

## Run the examples

Use the same all-test commands from
[lesson 2](02-property-based-testing.md#run-the-examples).
