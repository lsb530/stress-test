package com.boki.stresstest.listener

import com.boki.stresstest.ext.logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.env.ConfigurableEnvironment
import java.util.UUID

class AppReadyEventListener(
    private val serverUUID: UUID,
) : ApplicationListener<ApplicationReadyEvent>, Ordered {

    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE

    val ApplicationReadyEvent.timeTakenMs: Long
        get() = this.timestamp - this.applicationContext.startupDate

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { ">> ApplicationReadyEvent fired" }

        logAppStartDuration(event)
        logServerUUID()
        logSwaggerPath(event)
    }

    private fun logSwaggerPath(event: ApplicationReadyEvent) {
        val env: ConfigurableEnvironment = event.applicationContext.environment
        val port = env.getProperty("server.port", "8080")
        val contextPath = env.getProperty("server.servlet.context-path", "")

        logger.info { "=".repeat(60) }
        logger.info { "🚀 Application started successfully!" }
        logger.info { "📋 Server UUID: $serverUUID" }
        logger.info { "🌐 Swagger UI: http://localhost:$port$contextPath/swagger-ui/index.html" }
        logger.info { "📄 API Docs: http://localhost:$port$contextPath/v3/api-docs" }
        logger.info { "=".repeat(60) }

        // 콘솔에도 출력
        println("\n>>> Swagger UI: http://localhost:$port$contextPath/swagger-ui/index.html\n")
    }

    private fun logAppStartDuration(event: ApplicationReadyEvent): Unit {
        val appTakenMs = event.timeTakenMs
        val displayTime = when {
            appTakenMs >= 60_000 -> String.format("%.2f분", appTakenMs / 60_000.0)
            appTakenMs >= 1_000 -> String.format("%.2f초", appTakenMs / 1_000.0)
            else -> "${appTakenMs}밀리초"
        }
        logger.info { "## Application is ready (기동 시간: $displayTime)" }
    }

    private fun logServerUUID() {
        logger.info { "## Server ID: $serverUUID" }
    }

}