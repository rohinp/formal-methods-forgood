# Wiki

This wiki explains the techniques implemented in the repository from an
application developer's perspective. It introduces the formal terminology, but
focuses on what guarantee a technique buys in ordinary code.

## Contents

- [Learning roadmap](ROADMAP.md)
- [01 — Contracts](01-contracts.md)
- [02 — Property-based testing](02-property-based-testing.md)
- [03 — Domain modeling with types](03-domain-modeling-with-types.md)
- [04 — Data and class invariants](04-data-and-class-invariants.md)

Each completed technique should include:

1. the problem it addresses;
2. the relevant jargon in plain language;
3. what is and is not guaranteed;
4. the idiomatic approach in Python, Scala 3, and Kotlin;
5. links to executable examples and authoritative references.

## Repository layout

Production examples are grouped by domain; verification code is grouped by
formal-method technique. The same concepts therefore appear in predictable
places in every language:

```text
python/src/banking/                 Python production code
python/tests/contracts/             Python contract tests
python/tests/propertytesting/       Python property tests
python/tests/domainmodeling/         Python domain-model tests
python/tests/invariants/             Python invariant tests

scala/src/main/scala/banking/       Scala production code
scala/src/test/scala/contracts/      Scala contract tests
scala/src/test/scala/propertytesting/
scala/src/test/scala/domainmodeling/
scala/src/test/scala/invariants/

kotlin/src/main/kotlin/banking/     Kotlin production code
kotlin/src/test/kotlin/contracts/    Kotlin contract tests
kotlin/src/test/kotlin/propertytesting/
kotlin/src/test/kotlin/domainmodeling/
kotlin/src/test/kotlin/invariants/
```

The extra `main/scala`, `test/scala`, `main/kotlin`, and `test/kotlin` levels are
the conventional JVM build-tool layouts.
