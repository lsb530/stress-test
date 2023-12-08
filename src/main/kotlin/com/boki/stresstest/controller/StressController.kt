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
        val n = 10000 // 범위 증가
        for (i in 2 until n) {
            for (j in 2 until n) { // 추가적인 반복
                isPrime(i * j) // 계산량 증가
            }
        }
        return "Completed CPU intensive task"
    }

    @GetMapping("/memory-intensive")
    fun memoryIntensiveTask(): String {
        val largeList = MutableList(100_000_000) { it }
        return "Created a large list in memory"
    }

//    @GetMapping("/memory-intensive")
//    fun memoryIntensiveTask(): String {
//        val largeList = MutableList(50_000_000) { it } // 리스트 크기 증가
//        val anotherLargeList = largeList.map { it.toDouble() } // 추가 리스트 생성
//        return "Created a large list in memory"
//    }

//    @GetMapping("/memory-intensive")
//    fun memoryIntensiveTask(): String {
//        val largeArray = ByteArray(1024 * 1024 * 1024) // 1GB 크기의 바이트 배열 생성
//        // 배열을 참조하고 있는 동안 메모리에 유지
//        return "Created a large array in memory. First element: ${largeArray[0]}"
//    }

    private fun isPrime(n: Int): Boolean {
        for (i in 2 until n) {
            if (n % i == 0) {
                return false
            }
        }
        return true
    }
}