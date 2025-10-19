package com.boki.stresstest.util

import kotlin.random.Random

object MobileNumberGenerator {

  fun generateRandomMobileNumber(): String {
    val part1 = "%04d".format(Random.nextInt(0, 10_000))
    val part2 = "%04d".format(Random.nextInt(0, 10_000))
    return "010-$part1-$part2"
  }

}