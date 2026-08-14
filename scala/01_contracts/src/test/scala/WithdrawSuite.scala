package contracts

class WithdrawSuite extends munit.FunSuite:
  test("withdraws part of a balance"):
    assertEquals(Withdraw.withdraw(100, 30), 70)

  test("may withdraw nothing"):
    assertEquals(Withdraw.withdraw(100, 0), 100)

  test("may withdraw the entire balance"):
    assertEquals(Withdraw.withdraw(100, 100), 0)

  test("rejects a negative balance"):
    interceptMessage[IllegalArgumentException]("requirement failed: balance must be non-negative"):
      Withdraw.withdraw(-1, 0)

  test("rejects a negative amount"):
    interceptMessage[IllegalArgumentException]("requirement failed: amount must be non-negative"):
      Withdraw.withdraw(100, -1)

  test("rejects an overdraft"):
    interceptMessage[IllegalArgumentException]("requirement failed: amount must not exceed balance"):
      Withdraw.withdraw(100, 101)

