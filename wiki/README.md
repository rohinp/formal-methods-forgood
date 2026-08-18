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
- [05 — State-machine and model-based testing](05-state-machine-model-based-testing.md)
- [06 — Exhaustive testing of small domains](06-exhaustive-testing-small-domains.md)
- [07 — Static and symbolic analysis](07-static-and-symbolic-analysis.md)
- [08 — Selective SMT solving and model checking](08-selective-smt-solving-model-checking.md)

Each completed technique should include:

1. the problem it addresses;
2. the relevant jargon in plain language;
3. what is and is not guaranteed;
4. the idiomatic approach in Python, Scala 3, and Kotlin;
5. links to executable examples and authoritative references.

## Applied examples

- [Semantic and LLM request caching](https://github.com/rohinp/pragmatic-developer-skills/tree/main/example)
  combines contracts, domain types, graph invariants, property-based testing,
  and exhaustive testing of a small intent domain across Python, Scala 3, and
  Kotlin. External embedding and LLM services are deterministic mocks. The
  example credits and adapts the idea from Manoj's
  [HackerNoon article](https://hackernoon.com/graph-theory-based-semantic-caching-scaling-llm-applications).

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
python/tests/statemachine/           Python model-based state-machine tests
python/tests/exhaustive/             Python bounded exhaustive tests
python/tests/modelchecking/          Python explicit-state model checker
python/tests/smt/                    Python runner for the shared SMT proof

scala/src/main/scala/banking/       Scala production code
scala/src/test/scala/contracts/      Scala contract tests
scala/src/test/scala/propertytesting/
scala/src/test/scala/domainmodeling/
scala/src/test/scala/invariants/
scala/src/test/scala/statemachine/
scala/src/test/scala/exhaustive/
scala/src/test/scala/modelchecking/

kotlin/src/main/kotlin/banking/     Kotlin production code
kotlin/src/test/kotlin/contracts/    Kotlin contract tests
kotlin/src/test/kotlin/propertytesting/
kotlin/src/test/kotlin/domainmodeling/
kotlin/src/test/kotlin/invariants/
kotlin/src/test/kotlin/statemachine/
kotlin/src/test/kotlin/exhaustive/
kotlin/src/test/kotlin/modelchecking/

specs/                               Language-neutral formal specifications
```

The extra `main/scala`, `test/scala`, `main/kotlin`, and `test/kotlin` levels are
the conventional JVM build-tool layouts.
