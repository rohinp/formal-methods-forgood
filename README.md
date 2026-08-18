# Practical Formal Methods

A learning repository for applying useful parts of formal methods to everyday
software development in Python, Scala 3, and Kotlin.

The goal is not to cover the entire academic field. It is to learn techniques
that provide practical confidence in application code—especially code generated
or modified by AI agents—and eventually turn the lessons into an agent skill.

## How this project works

- We study one technique at a time.
- Each technique gets small, executable examples and tests.
- Implementations follow the conventions of each language rather than forcing
  them to look identical.
- We state what each technique guarantees, and what it cannot guarantee.

## Wiki

The [project wiki](wiki/README.md) contains the lessons, terminology, code links,
external references, and instructions for running the examples.

- [Learning roadmap and progress](wiki/ROADMAP.md)
- [Lesson 01 — Contracts](wiki/01-contracts.md)
- [Lesson 02 — Property-based testing](wiki/02-property-based-testing.md)
- [Lesson 03 — Domain modeling with types](wiki/03-domain-modeling-with-types.md)
- [Lesson 04 — Data and class invariants](wiki/04-data-and-class-invariants.md)
- [Lesson 05 — State-machine and model-based testing](wiki/05-state-machine-model-based-testing.md)
- [Lesson 06 — Exhaustive testing of small domains](wiki/06-exhaustive-testing-small-domains.md)
- [Lesson 07 — Static and symbolic analysis](wiki/07-static-and-symbolic-analysis.md)
- [Lesson 08 — Selective SMT solving and model checking](wiki/08-selective-smt-solving-model-checking.md)

The README stays intentionally general. Detailed learning material belongs in
the wiki.

## Future experiments

The completed lessons are the evidence base for the current verification skill.
Possible later experiments include mutation testing, metamorphic testing,
differential testing, fuzzing, fault injection, and concurrency/interleaving
testing. They will be tried here one technique at a time before any guidance is
promoted into the skill.

See [future candidates and their promotion criteria](wiki/ROADMAP.md#future-experiments).

## Applied example

The companion skills repository contains a
[semantic and LLM request caching example](https://github.com/rohinp/pragmatic-developer-skills/tree/main/example)
in Python, Scala 3, and Kotlin. It combines selected lessons from this
playground to protect graph structure, bounded traversal, and cache-hit
behavior while mocking embeddings and the external LLM. The graph-caching idea
is credited to Manoj's HackerNoon article,
[“Graph Theory-Based Semantic Caching: Scaling LLM Applications”](https://hackernoon.com/graph-theory-based-semantic-caching-scaling-llm-applications).
