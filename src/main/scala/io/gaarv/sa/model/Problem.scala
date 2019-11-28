package io.gaarv.sa.model

trait Problem[S <: Solution] {

  def randomSolution(): S

  def tweakSolution(solution: S): S

}
