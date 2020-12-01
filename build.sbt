import Dependencies._

lazy val root = (project in file(".")).settings(
  name := "scala-simulated-annealing",
  description := "scala-simulated-annealing",
  organization := "io.gaarv",
  version := "0.1",
  scalaVersion := "2.13.4",
  libraryDependencies += scalaLogging,
  libraryDependencies += scalaTest % Test
)
