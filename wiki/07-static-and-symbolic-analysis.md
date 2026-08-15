# 07 — Static and symbolic analysis

## Why a developer should care

Tests execute code with concrete inputs. Static and symbolic tools can inspect
code without waiting for a developer to write each example:

- **static analysis** finds problems from source code, types, and control flow;
- **symbolic analysis** follows paths using unknown symbolic values and asks a
  solver whether a contract can fail.

These tools are especially useful for AI-generated changes because they provide
fast, mechanically checked feedback before code is run in production.

## Jargon

- A **type checker** verifies that values are used according to their declared
  types.
- **Control-flow analysis** reasons about which program points and branches are
  reachable.
- An **exhaustiveness check** ensures every member of a closed set is handled.
- A **symbolic value** represents many possible concrete values at once.
- A **path condition** records the conditions required to reach one branch.
- A **verification condition** is a logical statement whose validity implies a
  program property.
- A **counterexample** is a concrete input showing that a claimed property can
  fail.
- **Path explosion** occurs when branches create too many paths to analyze
  within available time.

## The production calculation we analyze

The core calculation now has an explicit contract:

```text
pre:  balance >= 0
pre:  amount >= 0
pre:  amount <= balance
post: result >= 0
post: result == balance - amount
```

Python and Scala's real `withdraw` implementations call this small calculation.
Keeping the verification target pure and narrow makes automated reasoning much
more practical than pointing a symbolic tool at the whole mutable application.

## Python: mypy and CrossHair

Python remains dynamically typed at runtime, but `mypy` can statically check its
annotations. This project enables strict checking in
[`pyproject.toml`](../python/pyproject.toml). It currently reports no issues in
the production package:

```sh
cd python
mypy
```

The calculation in
[`verified_arithmetic.py`](../python/src/banking/verified_arithmetic.py) uses
PEP 316-style preconditions and postconditions in its docstring. CrossHair
executes it with symbolic integers and uses an SMT solver to search its paths:

```sh
PYTHONPATH=src crosshair check \
  --analysis_kind=PEP316 \
  --report_all \
  src/banking/verified_arithmetic.py
```

For the current implementation, CrossHair reports both postconditions as
“Confirmed over all paths.” If the subtraction is deliberately changed to
addition, it reports a concrete counterexample instead.

[Mypy documentation](https://mypy.readthedocs.io/) ·
[CrossHair introduction](https://crosshair.readthedocs.io/en/latest/introduction.html) ·
[CrossHair contracts](https://crosshair.readthedocs.io/en/latest/contracts.html)

This does not make all Python code statically safe. Unannotated code, `Any`,
reflection, native extensions, side effects, and unsupported operations can
weaken or block analysis. CrossHair may also time out or return an unknown
result on more complex functions.

## Scala 3: compiler checks and Stainless-compatible contracts

Scala already performs static type and pattern-match analysis. The build now
enables deprecation, feature, and unchecked warnings and promotes every warning
to an error:

```sh
cd scala
sbt compile
```

[`VerifiedArithmetic.scala`](../scala/src/main/scala/banking/VerifiedArithmetic.scala)
uses ordinary Scala `require` and `ensuring` expressions. The standard compiler
type-checks them, and they run as runtime assertions, but `scalac` does **not**
prove them.

Stainless is a separate verifier for a supported subset of Scala. It translates
such contracts into verification conditions and can prove them or produce
counterexamples:

```sh
stainless src/main/scala/banking/VerifiedArithmetic.scala
```

[Stainless introduction](https://epfl-lara.github.io/stainless/intro.html) ·
[Verification conditions](https://epfl-lara.github.io/stainless/verification.html) ·
[Installation](https://epfl-lara.github.io/stainless/installation.html)

Stainless is intentionally not installed into the normal sbt build. It accepts
a Scala subset, has its own verifier toolchain, and should be adopted for a
small verification-focused module rather than silently imposed on all
application code. The contract is Stainless-compatible, but this repository's
automated local result currently comes from `scalac` and runtime tests, not a
recorded Stainless proof.

## Kotlin: compiler analysis, without a forced symbolic tool

The Kotlin compiler performs static type and control-flow analysis. The Gradle
build now treats all compiler warnings as errors. Run it with:

```sh
cd kotlin
./gradlew compileKotlin
```

[`Withdraw.kt`](../kotlin/src/main/kotlin/banking/Withdraw.kt) models the decision
as a closed enum and handles it with a `when` expression. The compiler verifies
that both `ALLOWED` and `OVERDRAFT` are covered. If another enum member is added,
the build fails until the new case is handled.

[Kotlin exhaustiveness rules](https://kotlinlang.org/spec/kotlin-spec.html#exhaustive-when-expressions) ·
[Kotlin compiler options](https://kotlinlang.org/docs/compiler-reference.html)

Kotlin does not currently have a widely adopted, low-friction symbolic verifier
comparable to CrossHair for ordinary Python or Stainless for a Scala subset.
Research and JVM-bytecode tools exist, but adding one would conflict with this
project's goal of using techniques a broad range of Kotlin developers can
maintain. Kotlin's `require` and `check` remain runtime checks; the compiler does
not prove their arithmetic expressions.

## What the tools actually guarantee

| Check | Python | Scala 3 | Kotlin |
|---|---|---|---|
| Types checked before runtime | `mypy`, opt-in | Compiler | Compiler |
| Warnings fail the build | Strict mypy errors | `-Werror` | Gradle setting |
| Closed cases checked exhaustively | With suitable union plus type checker | Compiler | Compiler |
| Arithmetic contract analyzed symbolically here | CrossHair | Prepared for optional Stainless | No |
| Whole application proved correct | No | No | No |

A successful analysis result is always scoped to the tool's model, supported
language features, configuration, and written specification. A solver cannot
detect that the specification itself expresses the wrong business rule.

## Static analysis versus symbolic analysis

Static analysis is the broad category: it includes types, lints, data-flow, and
exhaustiveness checks. Symbolic execution is one deeper static technique that
explores code paths using symbolic inputs. Not every static analyzer uses a
solver, and a successful compilation is not a proof of functional correctness.

## Run the full examples

Install the updated Python development dependencies first:

```sh
cd python
pip install -r requirements-dev.txt
```

Then use the analysis commands above and the all-test commands from
[lesson 2](02-property-based-testing.md#run-the-examples).
