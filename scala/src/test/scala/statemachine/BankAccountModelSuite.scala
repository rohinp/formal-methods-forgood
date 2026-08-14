package statemachine

import banking.{Balance, BankAccount, WithdrawalAmount}
import banking.Withdraw.WithdrawalError.AmountExceedsBalance
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class BankAccountModelSuite extends munit.ScalaCheckSuite:
  private final case class AccountModel(
      balance: Int,
      successfulWithdrawals: Vector[Int]
  ):
    def withdraw(amount: Int): (Boolean, AccountModel) =
      if amount > balance then (false, this)
      else
        (
          true,
          copy(
            balance = balance - amount,
            successfulWithdrawals = successfulWithdrawals :+ amount
          )
        )

  private val commandSequences =
    Gen.choose(0, 30).flatMap(size => Gen.listOfN(size, Gen.choose(0, 150)))

  private def balance(value: Int): Balance = Balance.from(value).toOption.get
  private def amount(value: Int): WithdrawalAmount =
    WithdrawalAmount.from(value).toOption.get

  property("generated command sequences match the model"):
    forAll(commandSequences) { commands =>
      var model = AccountModel(100, Vector.empty)
      var account = BankAccount.open(balance(100))

      commands.foreach { rawAmount =>
        val (expectedSuccess, nextModel) = model.withdraw(rawAmount)

        account.withdraw(amount(rawAmount)) match
          case Right(updatedAccount) =>
            assert(expectedSuccess, "the account accepted a model-rejected command")
            account = updatedAccount
          case Left(AmountExceedsBalance) =>
            assert(!expectedSuccess, "the account rejected a model-accepted command")

        model = nextModel
        assertEquals(account.balance.value, model.balance)
        assertEquals(
          account.withdrawals.map(_.value),
          model.successfulWithdrawals
        )
      }
    }
