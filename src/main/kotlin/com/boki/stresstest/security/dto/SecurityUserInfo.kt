package com.boki.stresstest.security.dto

import org.springframework.security.core.userdetails.UserDetails

data class SecurityUserInfo(
    val username: String,
    val authorities: List<String>
) {
    companion object {
        fun fromUserDetails(userDetails: UserDetails): SecurityUserInfo {
            return SecurityUserInfo(
                username = userDetails.username,
                authorities = userDetails.authorities.map { it.authority }
            )
        }
    }

    fun toLoggerString(): String {
        return "username: $username, authorities: ${authorities.joinToString(", ")}"
    }
}