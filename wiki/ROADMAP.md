# Learning roadmap

We will take one small step at a time. The order can change when an earlier
lesson exposes a useful prerequisite.

| # | Technique | Developer value | Status |
|---|---|---|---|
| 1 | Contracts | Make assumptions and promises executable | Complete |
| 2 | Property-based testing | Check general rules over many generated inputs | Complete |
| 3 | Domain modeling with types | Make invalid states harder or impossible to construct | Complete |
| 4 | Data and class invariants | Preserve valid state across operations | Complete |
| 5 | State-machine/model-based testing | Verify sequences of operations | Complete |
| 6 | Exhaustive testing of small domains | Check every input when the domain is bounded | Complete |
| 7 | Static and symbolic analysis | Find paths and counterexamples without hand-written cases | Complete |
| 8 | Selective SMT solving/model checking | Prove focused properties of critical logic | Complete |

“Complete” means the introductory lesson exists in all applicable languages. It
does not mean the subject has been exhausted.

## Next milestone

The introductory curriculum and initial risk-based coding-agent skill are
complete. The first integrated example applies selected techniques to
[semantic and LLM request caching](https://github.com/rohinp/pragmatic-developer-skills/tree/main/example).
The next milestone is a controlled evaluation that compares agent work with and
without the skills using the same starting code and acceptance checks.

## Future experiments

These are candidates, not committed lessons. We will select one at a time when
it addresses a practical developer problem that the completed curriculum does
not cover well.

| Candidate | Developer question | Status |
|---|---|---|
| Mutation testing | Would the current tests detect plausible implementation mistakes? | Not started |
| Metamorphic testing | Can related inputs and outputs provide an oracle when exact expected results are difficult to state? | Not started |
| Differential testing | Do independent implementations disagree on the same input? | Not started |
| Fuzzing and adversarial input generation | Can malformed or unexpected input expose crashes, hangs, or invariant violations? | Not started |
| Fault injection and reliability properties | What remains true when dependencies fail, time out, retry, or partially complete? | Not started |
| Concurrency and interleaving testing | Do safety and progress properties survive different operation schedules? | Not started |

### Promotion rule

A candidate becomes skill guidance only after:

1. a small developer-focused example is executable in every applicable language;
2. the example demonstrates a failure that simpler existing techniques do not
   expose as clearly;
3. tests and reproduction commands are documented;
4. the wiki explains the guarantee, cost, and remaining limitations; and
5. the technique has a clear risk-based trigger and does not become mandatory
   ceremony for ordinary code.

Useful starting references:

- [PIT mutation testing concepts](https://pitest.org/quickstart/basic_concepts/)
- [Metamorphic testing review](https://doi.org/10.1145/3143561)
- [Differential Testing for Software](https://www.cs.swarthmore.edu/~bylvisa1/cs97/f13/Papers/DifferentialTestingForSoftware.pdf)
- [LLVM libFuzzer documentation](https://llvm.org/docs/LibFuzzer.html)
- [Principles of Chaos Engineering](https://principlesofchaos.org/)
- [JetBrains Lincheck](https://github.com/JetBrains/lincheck)
