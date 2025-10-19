package com.boki.stresstest.listener

import com.boki.stresstest.ext.logger
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.PropertiesPropertySource
import java.util.Properties
import java.util.UUID

class AppEnvPreparedListener(
    private val serverUUID: UUID,
) : ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    override fun onApplicationEvent(event: ApplicationEnvironmentPreparedEvent) {
        logger.info { ">> ApplicationEnvironmentPreparedEvent fired" }

        val env = event.environment
        if (env.activeProfiles.contains("prod")) {
            changeSwaggerDocPath(env)
            logger.info { ">> Swagger path configured for prod profile" }
        }
    }

    private fun changeSwaggerDocPath(env: ConfigurableEnvironment) {
        val props = Properties().apply {
            put("springdoc.swagger-ui.path", "/$serverUUID/swagger-docs")
            put("springdoc.api-docs.path", "/$serverUUID/v3/api-docs")
        }
        env.propertySources.addFirst(PropertiesPropertySource("dynamic-swagger-path", props))
    }
}