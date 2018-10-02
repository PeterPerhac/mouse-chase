val root = (project in file(".")).enablePlugins(ScalaJSPlugin)

name := "mouse-chase"

version := "1.0"

scalaVersion := "2.12.6"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings", "-Ypartial-unification")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.3.1",
  "org.scala-js" %%% "scalajs-dom" % "0.9.6"
)
