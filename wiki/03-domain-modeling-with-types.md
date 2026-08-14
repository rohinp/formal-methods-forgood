# 03 — Domain modeling with types

## Why a developer should care

Our first `withdraw` functions accepted two plain integers:

```text
withdraw(balance: Int, amount: Int)
```

That signature permits negative values and makes it easy to swap two numbers
that mean different things. We now introduce `Balance` and `WithdrawalAmount`:

```text
withdraw(balance: Balance, amount: WithdrawalAmount)
```

Each type accepts only a non-negative value. Code inside `withdraw` can rely on
that local fact instead of checking it again.

## Jargon

- A **domain type** represents a concept from the problem, rather than only its
  storage representation. `Balance` communicates more than `Int`.
- A **nominal type** is distinct because of its declared name. A balance and a
  withdrawal amount are different even though both contain integers.
- A **smart constructor** validates raw input before returning a domain value.
- **Parse, don't validate** means turning untrusted input into a validated type
  at the boundary, then using that stronger type internally.
- **Make illegal states unrepresentable** is the design goal. In ordinary code,
  a successfully constructed `Balance` cannot contain a negative number.

That last phrase is not absolute magic. Language escape hatches, reflection,
deserialization frameworks, integer overflow, and foreign-language interop can
weaken guarantees. The useful claim is always scoped to normal, type-checked
construction and use.

## What moved out of `withdraw`

Negative values are invalid independently, so their checks belong to the domain
types. An overdraft is different: `Balance(10)` and `WithdrawalAmount(20)` are
both valid alone, but invalid together for this operation. Therefore
`amount <= balance` remains an operation-level precondition.

## Python: runtime wrappers plus optional static checking

Python uses two frozen, slotted dataclasses. Their `__post_init__` methods reject
non-integers and negative values, and using separate classes preserves the
domain names at runtime.

[Domain types](../python/src/banking/money.py) ·
[Tests](../python/tests/domainmodeling/test_money.py) ·
[Python dataclasses](https://docs.python.org/3/library/dataclasses.html) ·
[Python typing](https://docs.python.org/3/library/typing.html)

### Dynamic-language limitations

Python does **not** enforce function annotations at runtime. A static type
checker can reject a swapped `Balance` and `WithdrawalAmount`, but running the
program without that checker does not guarantee the call-site types. Python's
own documentation states that annotations are not runtime-enforced.

The constructors still provide useful runtime validation, and `frozen=True`
prevents normal reassignment. However, Python documents frozen dataclasses as
emulated—not truly immutable—and advanced code can bypass normal safeguards.
This makes invalid states difficult and obvious, not impossible under every
runtime technique.

We intentionally use real wrapper classes rather than `typing.NewType` because
`NewType` is only distinct to a static checker and returns its argument unchanged
at runtime.

## Scala 3: opaque types and smart constructors

Scala uses the opaque types `Balance` and `WithdrawalAmount`. Outside their
definition scope they are distinct compile-time types, while at runtime each can
use the underlying `Int` representation without wrapper allocation.

Raw integers enter through `Balance.from` and `WithdrawalAmount.from`, which
return `Either` so construction failure is explicit. A compile-time test verifies
that a `WithdrawalAmount` cannot be assigned where a `Balance` is expected.

[Domain types](../scala/src/main/scala/banking/Money.scala) ·
[Tests](../scala/src/test/scala/domainmodeling/MoneySuite.scala) ·
[Scala opaque types](https://docs.scala-lang.org/scala3/reference/other-new-features/opaques-details.html) ·
[Scala domain modeling](https://docs.scala-lang.org/scala3/book/domain-modeling-intro.html)

Opaque types are transparent inside their defining scope, so that scope must not
leak unvalidated values. Runtime input still needs the smart constructors, and
our types do not encode the relationship `amount <= balance`.

## Kotlin: value classes

Kotlin uses two `@JvmInline value class` declarations. They are distinct types
to the Kotlin compiler even though the JVM can represent them using their
underlying integers. Each `init` block rejects a negative value.

[Domain types](../kotlin/src/main/kotlin/banking/Money.kt) ·
[Tests](../kotlin/src/test/kotlin/domainmodeling/MoneyTest.kt) ·
[Kotlin value classes](https://kotlinlang.org/docs/inline-classes.html) ·
[Kotlin type-safe value idiom](https://kotlinlang.org/docs/idioms.html#use-inline-value-classes-for-type-safe-values)

Validation still occurs at runtime; the Kotlin compiler does not prove that an
arbitrary integer is non-negative. Value-class boxing and Java interoperability
also vary by usage. As in Scala, these types cannot by themselves express the
relationship between a particular balance and withdrawal amount.

## What this lesson guarantees

| Guarantee | Python | Scala 3 | Kotlin |
|---|---|---|---|
| Normal construction rejects negative values | Runtime | Runtime via smart constructor | Runtime |
| Balance and amount are distinct runtime classes | Yes | Usually erased to `Int` | May be unboxed to `Int` |
| Compiler rejects swapped argument types | Only with a separate type checker | Yes | Yes |
| `amount <= balance` encoded in the types | No | No | No |

Types reduce the number of states the rest of the program must handle. They do
not prove the withdrawal algorithm, replace boundary validation, or express all
relationships between values. Contracts and property tests continue to cover
those different jobs.

## Run the examples

Use the same all-test commands from [lesson 2](02-property-based-testing.md#run-the-examples).
