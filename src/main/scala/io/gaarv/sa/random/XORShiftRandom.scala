package io.gaarv.sa.random

import java.nio.ByteBuffer
import java.util.{ Random => JavaRandom }

import scala.util.hashing.MurmurHash3

class XORShiftRandom(seed: Long) extends JavaRandom(seed) {

  var currentSeed: Long = XORShiftRandom.hashSeed(seed)

  override def setSeed(s: Long): Unit = currentSeed = XORShiftRandom.hashSeed(s)

  // we need to just override next - this will be called by nextInt, nextDouble,
  // nextGaussian, nextLong, etc.
  override protected def next(bits: Int): Int = {
    var nextSeed = currentSeed ^ (currentSeed << 21)
    nextSeed ^= (nextSeed >>> 35)
    nextSeed ^= (nextSeed << 4)
    currentSeed = nextSeed
    (nextSeed & ((1L << bits) - 1)).asInstanceOf[Int]
  }

}

object XORShiftRandom {

  private def hashSeed(seed: Long): Long = {
    val bytes    = ByteBuffer.allocate(java.lang.Long.SIZE).putLong(seed).array()
    val lowBits  = MurmurHash3.bytesHash(bytes)
    val highBits = MurmurHash3.bytesHash(bytes, lowBits)
    (highBits.toLong << 32) | (lowBits.toLong & 0xFFFFFFFFL)
  }

}
