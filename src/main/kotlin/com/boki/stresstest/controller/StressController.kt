package com.boki.stresstest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StressController {


//    @GetMapping("/cpu-intensive")
//    fun cpuIntensiveTask(): String {
//        val n = 10000
//        for (i in 2 until n) {
//            isPrime(i)
//        }
//        return "Completed CPU intensive task"
//    }

    @GetMapping("/cpu-intensive")
    fun cpuIntensiveTask(): String {
        val n = 50000 // 범위 증가
        for (i in 2 until n) {
            for (j in 2 until n) { // 추가적인 반복
                isPrime(i * j) // 계산량 증가
            }
        }
        return "Completed CPU intensive task"
    }

//    @GetMapping("/memory-intensive")
//    fun memoryIntensiveTask(): String {
//        val largeList = MutableList(100_000_000) { it }
//        return "Created a large list in memory"
//    }

    @GetMapping("/memory-intensive")
    fun memoryIntensiveTask(): String {
        val largeList = MutableList(500_000_000) { it } // 리스트 크기 증가
        val anotherLargeList = largeList.map { it.toDouble() } // 추가 리스트 생성
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