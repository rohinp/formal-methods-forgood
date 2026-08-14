ThisBuild / scalaVersion := "3.7.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "formal-methods-playground",
    Compile / unmanagedSourceDirectories += baseDirectory.value / "scala" / "01_contracts" / "src" / "main" / "scala",
    Test / unmanagedSourceDirectories += baseDirectory.value / "scala" / "01_contracts" / "src" / "test" / "scala",
    libraryDependencies += "org.scalameta" %% "munit" % "1.1.1" % Test
  )
