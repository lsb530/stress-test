package com.boki.stresstest.common.interceptor

import com.boki.stresstest.ext.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class ClientRequestInterceptor() : HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any, ex: Exception?
    ) {
        logger.debug { "Handled ${request.method} ${request.requestURI}" }
    }

}