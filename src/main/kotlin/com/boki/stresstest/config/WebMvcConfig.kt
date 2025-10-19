package com.boki.stresstest.config

import com.boki.stresstest.common.converter.IgnoreCaseEnumConverter
import com.boki.stresstest.common.interceptor.ClientRequestInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val clientRequestInterceptor: ClientRequestInterceptor,
    private val ignoreCaseEnumConverter: IgnoreCaseEnumConverter,
) : WebMvcConfigurer {

    private val excludePathPatternList by lazy {
        listOf(
            "/assets/**",
            "/js/**",
            "/css/**",
            "/favicon.ico",
            "/error",
            "/admin/**",
            "h2**",
            "/h2/**",
            "/webjars/**",
            "/actuator/**"
        )
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",
                "http://localhost:8080",
            )
            .allowedOriginPatterns(
                "http://boki-lb-*.ap-northeast-2.elb.amazonaws.com",
                "https://boki-lb-*.ap-northeast-2.elb.amazonaws.com",
            )
            .allowedMethods("*")
            .allowedHeaders("*")
            .exposedHeaders("Authorization", "Set-Cookie")
            .allowCredentials(true)
    }


    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverterFactory(ignoreCaseEnumConverter)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(clientRequestInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(excludePathPatternList)
    }

}
