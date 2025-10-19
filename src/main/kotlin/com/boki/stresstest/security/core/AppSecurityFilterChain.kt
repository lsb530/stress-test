package com.boki.stresstest.security.core

import com.boki.stresstest.common.constant.API_V1_ADMIN
import com.boki.stresstest.common.constant.API_V1_USERS
import com.boki.stresstest.common.filter.MDCLoggingFilter
import com.boki.stresstest.security.exception.AccessDeniedHandler
import com.boki.stresstest.security.exception.AuthenticationEntryPoint
import com.boki.stresstest.security.filter.JwtAuthenticationFilter
import com.boki.stresstest.security.filter.JwtVerificationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class AppSecurityFilterChain {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtVerificationFilter: JwtVerificationFilter,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        mdcLoggingFilter: MDCLoggingFilter,
        authenticationEntryPoint: AuthenticationEntryPoint,
        accessDeniedHandler: AccessDeniedHandler,
    ): SecurityFilterChain {
        val excludePaths = arrayOf(
            "/health-check",
            "/error",
            "/actuator/**",
            "/favicon.ico",
            "/webjars/**",
            "/h2-console/**",

            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-docs/**",
            "/v3/api-docs/**",

            // "/$serverUUID/swagger-ui/**",
            // "/$serverUUID/swagger-docs/**",
            // "/$serverUUID/v3/api-docs/**",
            //
            // "$API_V1_ADMIN/**",
            // "$API_V1_AUTH/refresh",
            // "$API_V1_AUTH/refresh-cookie",
            // "$API_V1_AUTH/kakao",
            // "$API_V1_AUTH_NICE/**",
            // "$API_V1_AUTH/custom-token/**",
            // "$API_V1_SSO/**",
            // "$API_V1_FAQ/**",
            // "$API_V1_QNA/**",
            // "$API_V1_PRODUCTS/**",
            // "$API_V1_ORDERS/**",
            // "$API_V1_PAYMENTS/**",
        )

        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .cors { }
            .headers { it.frameOptions { opts -> opts.sameOrigin() } } // for h2
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.POST, API_V1_USERS).permitAll()
                    .requestMatchers(*excludePaths).permitAll()
                    .requestMatchers("$API_V1_ADMIN/**").hasRole("ADMIN")
                    .anyRequest().fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtVerificationFilter, jwtAuthenticationFilter::class.java)
            .addFilterBefore(mdcLoggingFilter, jwtVerificationFilter::class.java)
        return http.build()
    }

}