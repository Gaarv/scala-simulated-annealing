package io.gaarv.random

import scala.collection.mutable
import scala.util.Random

object RandomUtils {

  val MAX_FLOATING_POINT = 0.000001

  val random = new XORShiftRandom(System.nanoTime())

  def halfUp(d: Double): Int = ((d.abs * 2 + 1) / 2).toInt * (if (d > 0) 1 else -1)

  def almostEqual(d1: Double, d2: Double): Boolean = Math.abs(d1 - d2) <= MAX_FLOATING_POINT

  def randomIntBetween(min: Int, max: Int): Int = min + random.nextInt((max - min) + 1)

  def randomFrom[T](xs: Seq[T]): T = xs(random.nextInt(xs.size))

  def randomUntilDifferent(bound: Int, x: Int): Int = {
    var r = random.nextInt(bound)
    while (r == x) {
      r = random.nextInt(bound)
    }
    r
  }

  def randomUntilDifferent(bound: Int, xs: Seq[Int]): Int = {
    var r = random.nextInt(bound)
    while (xs.contains(r)) {
      r = randomFrom(Vector.range(0, bound).diff(xs))
    }
    r
  }

  def shufflePart(xs: Seq[Int], start: Int, end: Int): Seq[Int] = {
    val buffer: mutable.Buffer[Int] = xs.toBuffer
    val indices                     = (start.max(0) until end.min(xs.size - 1)).toVector
    val shuffled = indices.zip(Random.shuffle(indices) map { i =>
      buffer(i)
    })
    shuffled.foreach { t =>
      buffer.update(t._1, t._2)
    }
    buffer.toVector
  }

}
