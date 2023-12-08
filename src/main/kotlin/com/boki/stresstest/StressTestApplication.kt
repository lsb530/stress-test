package com.boki.stresstest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StressTestApplication

fun main(args: Array<String>) {
    runApplication<StressTestApplication>(*args)
}
