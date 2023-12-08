package com.boki.stresstest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StressController {

    @GetMapping("/cpu-intensive")
    fun cpuIntensiveTask(): String {
        val n = 10000
        for (i in 2 until n) {
            isPrime(i)
        }
        return "Completed CPU intensive task"
    }

    @GetMapping("/memory-intensive")
    fun memoryIntensiveTask(): String {
        val largeList = MutableList(100_000_000) { it }
        return "Created a large list in memory"
    }

    private fun isPrime(n: Int): Boolean {
        for (i in 2 until n) {
            if (n % i == 0) {
                return false
            }
        }
        return true
    }
}