package com.boki.stresstest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StressController {

    @GetMapping("/cpu-intensive")
    fun cpuIntensiveTask(): String {
        val n = 10_000 // 범위 증가
        for (i in 2 until n) {
            for (j in 2 until n) { // 추가적인 반복
                isPrime(i * j) // 계산량 증가
            }
        }
        return "Completed CPU intensive task"
    }

    @GetMapping("/memory-intensive")
    fun memoryIntensiveTask(): String {
        val size = 1_000_000_000 // 각 배열 당 약 1GB
        val arrays = mutableListOf<ByteArray>()

        for (i in 1..20) { // 20번 반복
            arrays.add(ByteArray(size))
        }

        return "Created ${arrays.size} large arrays in memory."
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