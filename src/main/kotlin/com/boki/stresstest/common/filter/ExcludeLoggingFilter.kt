package com.boki.stresstest.common.filter

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

class ExcludeLoggingFilter : Filter<ILoggingEvent>() {

    private val excludePatterns = listOf(
        "/health-check",
        "/swagger-ui",
        "/swagger-docs",
        "/v3/api-docs",
        "/webjars/",
        "/favicon.ico",
        "/h2-console",
        "/flutter_service_worker.js"
    )

    override fun decide(event: ILoggingEvent): FilterReply {
        val uri = event.mdcPropertyMap["uri"] ?: ""
        val message = event.formattedMessage

        // 제외할 패턴 중 하나라도 매칭되면 로그 제외
        if (excludePatterns.any { pattern ->
                message.contains(pattern) || uri.contains(pattern, ignoreCase = true)
            }
        ) {
            return FilterReply.DENY
        }

        return FilterReply.NEUTRAL
    }

}