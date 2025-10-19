package com.boki.stresstest.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class SwaggerConfig {

    @Bean
    @Profile("default", "local")
    fun apiServerLocal(): Server {
        return Server()
            .url("http://localhost:8080")
            .description("로컬 개발용 서버")
    }

    @Bean
    @Profile("dev", "prod")
    fun apiServerProd(): Server {
        return Server()
            .url("https://api.boki.co.kr")
            .description("보키 개발/운영 서버")
    }

    @Bean
    fun openAPIConfiguration(server: Server): OpenAPI {
        val bearerJWTScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)
            .name("Authorization")
            .description("인증 헤더 - Access Token Header")

        val refreshScheme = SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)
            .name("Refresh-Token")
            .description("Refresh token Header")

        val components = Components()
            .addSecuritySchemes("authorization", bearerJWTScheme)
            .addSecuritySchemes("refreshToken", refreshScheme)

        val openAPI = OpenAPI()
            .servers(listOf(server))
            .info(
                Info().title("BOKI API")
                    .description("보키 API 문서")
                    .version("v1.0.0")
                    .license(License().name("Apache 2.0").url("https://springdoc.org"))
            )
            .components(components)

        return openAPI
    }

}
