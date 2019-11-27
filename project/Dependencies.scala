import sbt._

object Dependencies {

  val logbackVersion = "1.2.3"

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  lazy val scalaTest    = "org.scalatest"              %% "scalatest"     % "3.0.8"

}
