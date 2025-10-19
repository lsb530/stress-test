package com.boki.stresstest.security.filter

import com.boki.stresstest.common.exception.code.ErrorCode
import com.boki.stresstest.security.dto.SecurityUserInfo
import com.boki.stresstest.security.jwt.JwtAdminUserDetailsService
import com.boki.stresstest.security.jwt.JwtService
import com.boki.stresstest.security.jwt.JwtUserDetailsService
import com.boki.stresstest.security.role.UserRole
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.boot.web.servlet.filter.OrderedFilter
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtAdminUserDetailsService: JwtAdminUserDetailsService,
    private val jwtUserDetails: JwtUserDetailsService,
    private val jwtService: JwtService,
) : OncePerRequestFilter(), OrderedFilter {

    override fun getOrder(): Int = 2

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substringAfter("Bearer ")
            val usernameFromToken = jwtService.parseUsername(token)
            if (SecurityContextHolder.getContext().authentication == null) {
                val authorities = jwtService.extractUserRoles(token)
                try {
                    val userDetails: UserDetails = if (authorities.contains(UserRole.ADMIN)) {
                        jwtAdminUserDetailsService.loadUserByUsername(usernameFromToken)
                    }
                    else {
                        jwtUserDetails.loadUserByUsername(usernameFromToken)
                    }

                    if (usernameFromToken == userDetails.username) {
                        val authToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities
                        )
                        MDC.put(
                            "authentication",
                            SecurityUserInfo.fromUserDetails(authToken.principal as UserDetails).toLoggerString()
                        )
                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                } catch (e: UsernameNotFoundException) {
                    logger.warn(e.message)
                    handleAuthenticationError(response, ErrorCode.NOT_FOUND_USER)
                    return
                } catch (e: Exception) {
                    logger.warn(e.message)
                    handleAuthenticationError(response, ErrorCode.FAILED_LOGIN)
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun handleAuthenticationError(
        response: HttpServletResponse,
        code: ErrorCode,
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json;charset=UTF-8"

        val errorResponse = """
      {
        "code": "${code.code}",
        "message": "${code.message}"
      }
    """.trimIndent()

        response.writer.write(errorResponse)
    }

}