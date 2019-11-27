package io.gaarv.algorithm

import java.lang.Math.exp

import io.gaarv.Logging
import io.gaarv.model.{Problem, Solution}
import io.gaarv.random.XORShiftRandom

import scala.annotation.tailrec
import scala.concurrent.duration.Duration

class SimulatedAnnealing[S <: Solution](
    initialTemperature: Double,
    coolingRate: Double,
    schedule: Int,
    absoluteTemperature: Double,
    maxDuration: Duration,
    debug: Boolean = false,
    debugRate: Int = 10000,
    optimum: Option[Double] = None
) extends Logging {

  def search(problem: Problem[S], initialSolution: S): S = {

    val random = new XORShiftRandom(System.nanoTime())
    val mark   = System.nanoTime()

    @tailrec
    def _search(solution: S, epoch: Int, elapsed: Long, temperature: Double, schedule: Int, bestSolution: S): S =
      if (temperature <= absoluteTemperature || elapsed >= maxDuration.toNanos || bestSolution.quality <= optimum.getOrElse(Double.MinValue))
        bestSolution
      else {
        val newSolution           = problem.tweakSolution(solution)
        val newEnergy             = newSolution.quality
        val currentEnergy         = solution.quality
        val newBestSolution       = if (newSolution.quality < bestSolution.quality) newSolution else bestSolution
        val acceptanceProbability = if (newEnergy < currentEnergy) 1.0 else exp((currentEnergy - newEnergy) / temperature)
        val newBest               = if (acceptanceProbability > random.nextDouble()) newSolution else solution
        val newSchedule           = if (epoch == schedule) Math.ceil(schedule * 1.1).toInt else schedule
        val newTemperature        = if (epoch == schedule) temperature * coolingRate else temperature
        val elapsed               = System.nanoTime() - mark
        if (debug && epoch % debugRate == 0) logger.info(s"epoch: $epoch, quality: ${newBest.quality}, temperature: ${newTemperature % 1.2f}")
        _search(newBest, epoch + 1, elapsed, newTemperature, newSchedule, newBestSolution)
      }

    _search(initialSolution, 1, 0, initialTemperature, schedule, initialSolution)
  }
}
