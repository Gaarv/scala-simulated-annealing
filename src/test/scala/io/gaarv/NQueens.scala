package io.gaarv

import io.gaarv.model.{ Problem, Solution }
import io.gaarv.algorithm.SimulatedAnnealing
import io.gaarv.random.RandomUtils._
import org.scalatest.{ Matchers, WordSpec }

import scala.concurrent.duration._

class NQueens extends WordSpec with Matchers {

  case class Queen(i: Int, j: Int) {
    def isAttacked(other: Queen): Boolean       = i == other.i || j == other.j || (other.i - i).abs == (other.j - j).abs
    def isNotSafe(others: List[Queen]): Boolean = others.exists(this.isAttacked)
    def isSafe(others: List[Queen]): Boolean    = !isNotSafe(others)
    def isAttackedBy(others: List[Queen]): Int  = others.count(this.isAttacked)
  }

  object NQueenGame {
    def showBoard(solution: List[Queen]): Unit = {
      val board = for {
        j <- 1 to solution.size
        i <- 1 to solution.size
      } yield Queen(i, j)
      board.foreach { position =>
        if (solution.contains(position)) print("Q ") else print(". ")
        if (position.i == solution.size) println()
      }
    }
  }

  case class NQueensSolution(queens: List[Queen]) extends Solution {
    def quality: Double = queens.map(q => q.isAttackedBy(queens.filter(_ != q))).sum[Int]
  }

  object Problem extends Problem[NQueensSolution] {

    var size: Int = _

    def randomSolution(): NQueensSolution = NQueensSolution(List.fill(size)(Queen(randomIntBetween(1, size), randomIntBetween(1, size))))

    def tweakSolution(solution: NQueensSolution): NQueensSolution = {
      val attacks      = solution.queens.map(q => q.isAttackedBy(solution.queens))
      val mostAttacked = attacks.indexOf(attacks.max)
      val candidates = for {
        _           <- 1 to 10
        randomQueen = Queen(randomIntBetween(1, size), randomIntBetween(1, size))
        candidate   = NQueensSolution(solution.queens.updated(mostAttacked, randomQueen))
      } yield candidate
      candidates.minBy(_.quality)
    }
  }

  "should be true if queen attacked" in {
    val q1 = Queen(2, 2)
    val q2 = Queen(2, 3)
    val q3 = Queen(3, 2)
    val q4 = Queen(1, 1)
    q1.isNotSafe(List(q2, q3, q4)) shouldBe true
  }

  "should be true if queen not attacked" in {
    val q1 = Queen(1, 1)
    val q2 = Queen(8, 2)
    val q3 = Queen(7, 4)
    val q4 = Queen(6, 3)
    q1.isSafe(List(q2, q3, q4)) shouldBe true
  }

  "should solve the NQueens Problem with SA" in {
    Problem.size = 8
    val randomBoard = Problem.randomSolution()
    NQueenGame showBoard randomBoard.queens
    val sa = new SimulatedAnnealing[NQueensSolution](
      initialTemperature = 100,
      coolingRate = 0.97,
      schedule = 1,
      absoluteTemperature = 1,
      maxDuration = 1.second
    )
    val best = sa.search(Problem, randomBoard)
    NQueenGame showBoard best.queens
    best.quality shouldBe 0
  }

}
