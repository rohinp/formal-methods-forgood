package modelchecking

import scala.collection.mutable

class AccountModelCheckerSuite extends munit.FunSuite:
  private val OpeningBalance = 3
  private val Commands = 0 to OpeningBalance

  private final case class ModelState(balance: Int, withdrawn: Int)
  private final case class Trace(state: ModelState, commands: Vector[Int])

  private def transition(state: ModelState, amount: Int): ModelState =
    if amount > state.balance then state
    else
      ModelState(
        balance = state.balance - amount,
        withdrawn = state.withdrawn + amount
      )

  test("every reachable state preserves the invariant"):
    val initial = ModelState(OpeningBalance, 0)
    val visited = mutable.Set(initial)
    val frontier = mutable.Queue(Trace(initial, Vector.empty))
    var evaluatedTransitions = 0

    while frontier.nonEmpty do
      val current = frontier.dequeue()
      val state = current.state
      assert(state.balance >= 0, s"negative balance after ${current.commands}")
      assertEquals(
        state.balance + state.withdrawn,
        OpeningBalance,
        s"conservation invariant failed after ${current.commands}"
      )

      Commands.foreach { amount =>
        evaluatedTransitions += 1
        val nextState = transition(state, amount)
        if visited.add(nextState) then
          frontier.enqueue(Trace(nextState, current.commands :+ amount))
      }

    assertEquals(
      visited.toSet,
      Set(
        ModelState(3, 0),
        ModelState(2, 1),
        ModelState(1, 2),
        ModelState(0, 3)
      )
    )
    assertEquals(evaluatedTransitions, 16)
