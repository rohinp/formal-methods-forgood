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

The introductory curriculum is complete. The next milestone is to distill these
lessons into a practical verification policy and, eventually, an AI coding-agent
skill that selects techniques according to the code's risk and shape.
