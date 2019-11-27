package io.gaarv.model

trait Problem[S <: Solution] {

  def randomSolution(): S

  def tweakSolution(solution: S): S

}
