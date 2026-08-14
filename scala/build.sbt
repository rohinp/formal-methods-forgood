ThisBuild / scalaVersion := "3.7.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "formal-methods-playground",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.2.0" % Test,
      "org.scalameta" %% "munit-scalacheck" % "1.2.0" % Test
    )
  )
