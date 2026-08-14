# Formal Methods Playground

Small, executable examples of practical formal methods in Python, Scala 3, and
Kotlin.

## Lesson 1: contracts

We specify a withdrawal operation as:

```text
withdraw(balance, amount) -> new_balance

Preconditions (the caller must provide):
- balance >= 0
- amount >= 0
- amount <= balance

Postconditions (the implementation must guarantee):
- new_balance >= 0
- new_balance == balance - amount
```

Run the Python example and tests:

```sh
python3 -m unittest discover -s python/01_contracts -p 'test_*.py' -v
```

Run the Scala example and tests:

```sh
sbt test
```

Run the Kotlin example and tests:

```sh
cd kotlin
./gradlew test
```

The important idea is not the syntax. A contract turns assumptions and promises
that might otherwise live only in a programmer's head into executable checks.
