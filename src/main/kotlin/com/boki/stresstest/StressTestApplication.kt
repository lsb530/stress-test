package com.boki.stresstest

import com.boki.stresstest.listener.AppEnvPreparedListener
import com.boki.stresstest.listener.AppReadyEventListener
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.TimeZone
import java.util.UUID

@EnableJpaAuditing
@SpringBootApplication
class StressTestApplication {
    companion object {
        private val serverUuid = UUID.randomUUID()

        @JvmStatic
        @Bean
        fun serverUUID(): UUID = serverUuid
    }
}

fun main(args: Array<String>) {
    // runApplication<StressTestApplication>(*args)

    setTimeZone()
    val serverUUID = StressTestApplication.serverUUID()
    val app = SpringApplication(StressTestApplication::class.java)
    app.addListeners(AppReadyEventListener(serverUUID))
    app.addListeners(AppEnvPreparedListener(serverUUID))

    app.run(*args)
}

fun setTimeZone() {
    val activeProfiles1 = System.getProperty("spring.profiles.active")
        ?.split(",") ?: emptyList()
    val activeProfiles2 = System.getenv("SPRING_PROFILES_ACTIVE")
        ?.split(",") ?: emptyList()

    if ("prod" in activeProfiles1 || "prod" in activeProfiles2) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        println(">>> [prod] JVM 기본 타임존을 UTC 로 설정했습니다.")
    }
}
