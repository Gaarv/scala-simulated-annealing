import Dependencies._

name := "scala-simulated-annealing"
description := "scala-simulated-annealing"
organization := "io.gaarv"
version      := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.12"
githubOwner := "gaarv"
githubRepository := "scala-simulated-annealing"
githubTokenSource := TokenSource.Environment("GITHUB_TOKEN") || TokenSource.GitConfig("github.token")


lazy val root = (project in file(".")).settings(
  libraryDependencies += scalaLogging,
  libraryDependencies += scalaTest % Test
)
