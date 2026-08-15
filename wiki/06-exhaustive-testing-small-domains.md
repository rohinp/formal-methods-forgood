# 06 — Exhaustive testing of small domains

## Why a developer should care

Property-based testing samples many generated inputs. When an important input
domain is genuinely small, we can instead execute **every** case in that domain.

For this lesson we define a miniature withdrawal domain:

```text
balance ∈ 0..20
amount  ∈ 0..20
```

Its Cartesian product contains `21 × 21 = 441` input pairs. The test executes
all 441 and checks this specification:

```text
if amount <= balance: result == balance - amount
otherwise:             withdrawal is rejected
```

## Jargon

- An **input domain** is the set of values considered valid inputs to a test.
- A **Cartesian product** contains every combination drawn from multiple sets.
- **Exhaustive testing** executes every element of the declared input domain.
- **Bounded exhaustive testing** executes every valid input up to an explicit
  developer-chosen bound.
- **State-space explosion** is the rapid growth in combinations as values,
  parameters, or operation sequences are added.
- A **bounded guarantee** is complete only inside the stated bound.

Software testing normally selects a finite subset from a much larger execution
domain, as explained by the
[Software Engineering Body of Knowledge](https://sfia-online.org/en/tools-and-resources/bodies-of-knowledge/swebok-software-engineering-body-of-knowledge/swebok-sfia8-the-guide-to-the-software-engineering-body-of-knowledge).
Bounded exhaustive testing makes that subset systematic and complete.

## The important boundary

Passing this lesson means:

> `withdraw` matches the specification for every balance and amount from 0
> through 20.

It does **not** mean:

> `withdraw` is correct for every integer.

The bound is part of the claim and must be visible in the test and its
documentation. Alloy applies the same discipline to bounded model analysis: a
property holding in a small scope is useful but is not guaranteed in larger
scopes. This idea is described as the
[small scope hypothesis](https://alloytools.org/tutorials/online/maintext-FS-1.html).

## Why not enumerate everything?

Python integers are unbounded, so their complete integer domain is infinite.
Scala and Kotlin `Int` values are finite, but all pairs would still contain
`2^64` combinations. Adding parameters multiplies the count; adding command
sequences makes it grow exponentially with sequence length.

Use exhaustive testing when the natural domain is small—for example:

- an enum or finite set of statuses;
- permission combinations represented by a few booleans;
- small protocol states and commands;
- parsers or algorithms explicitly limited to short inputs;
- a carefully chosen bounded version of a larger problem.

For large domains, use properties, partitions, boundary cases, generated
examples, or more specialized verification techniques.

## Language implementations

No additional library is needed. Each test uses ordinary nested loops and
counts the executed cases. The final `441` assertion prevents an accidental
range change from silently weakening the intended scope.

### Python

`subTest` records the current balance and amount so a failure identifies the
exact pair.

[Exhaustive test](../python/tests/exhaustive/test_withdraw_exhaustively.py) ·
[Python `range`](https://docs.python.org/3/library/stdtypes.html#range)

### Scala 3

Scala ranges enumerate the same product. Domain values enter through the smart
constructors from lesson 3, while `Either` makes acceptance and rejection
explicit.

[Exhaustive test](../scala/src/test/scala/exhaustive/WithdrawExhaustiveSuite.scala) ·
[Scala `Range`](https://www.scala-lang.org/api/current/scala/collection/immutable/Range.html)

### Kotlin

Kotlin closed ranges enumerate both inputs. The test checks the returned value
or the conventional `IllegalArgumentException` from the production API.

[Exhaustive test](../kotlin/src/test/kotlin/exhaustive/WithdrawExhaustiveTest.kt) ·
[Kotlin ranges](https://kotlinlang.org/docs/ranges.html)

## Static versus dynamic languages

Exhaustive testing is runtime execution in every language. Scala and Kotlin
statically check the test harness and domain-type usage; Python does not unless
a separate type checker runs. Static typing does not make an integer domain
small enough to enumerate, and dynamic typing does not prevent exhaustive
testing when we can define a finite domain precisely.

## Compared with property-based testing

| Property-based testing | Bounded exhaustive testing |
|---|---|
| Samples generated cases | Enumerates every bounded case |
| Scales to large domains | Limited by combinatorial growth |
| Often needs shrinking | Failing cases are deterministic; no shrinking is required |
| Guarantee depends on cases generated in that run | Complete for the declared bound |

Both approaches still depend on a correct oracle. Testing every input against a
wrong expected rule only confirms consistency with the wrong rule.

## Run the examples

Use the same all-test commands from
[lesson 2](02-property-based-testing.md#run-the-examples).
