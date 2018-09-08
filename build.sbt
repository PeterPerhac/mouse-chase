import com.lihaoyi.workbench.WorkbenchPlugin._

val root = (project in file(".")).enablePlugins(ScalaJSPlugin, WorkbenchPlugin)

workbenchSettings

name := "mouse-chase"

version := "1.0"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.6"
)


refreshBrowsers := refreshBrowsers.triggeredBy(fastOptJS in Compile).value

