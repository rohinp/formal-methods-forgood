package modelchecking

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val OPENING_BALANCE = 3
private val commands = 0..OPENING_BALANCE

private data class ModelState(
    val balance: Int,
    val withdrawn: Int,
)

private data class Trace(
    val state: ModelState,
    val commands: List<Int>,
)

private fun transition(state: ModelState, amount: Int): ModelState =
    if (amount > state.balance) {
        state
    } else {
        ModelState(
            balance = state.balance - amount,
            withdrawn = state.withdrawn + amount,
        )
    }

class AccountModelCheckerTest {
    @Test
    fun `every reachable state preserves the invariant`() {
        val initial = ModelState(balance = OPENING_BALANCE, withdrawn = 0)
        val visited = mutableSetOf(initial)
        val frontier = ArrayDeque<Trace>().apply { add(Trace(initial, emptyList())) }
        var evaluatedTransitions = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            val state = current.state
            val traceDescription = "commands=${current.commands}"

            assertTrue(state.balance >= 0, "negative balance after $traceDescription")
            assertEquals(
                OPENING_BALANCE,
                state.balance + state.withdrawn,
                "conservation invariant failed after $traceDescription",
            )

            commands.forEach { amount ->
                evaluatedTransitions += 1
                val nextState = transition(state, amount)
                if (visited.add(nextState)) {
                    frontier.add(Trace(nextState, current.commands + amount))
                }
            }
        }

        assertEquals(
            setOf(
                ModelState(3, 0),
                ModelState(2, 1),
                ModelState(1, 2),
                ModelState(0, 3),
            ),
            visited,
        )
        assertEquals(16, evaluatedTransitions)
    }
}
