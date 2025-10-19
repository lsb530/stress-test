package com.boki.stresstest.security.dto

import com.boki.stresstest.security.role.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class SecurityUser(
    val userId: Long,
    val role: UserRole,
) : User(
    userId.toString(),
    "",
    listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
) {
    fun isAdmin() = (role == UserRole.ADMIN)
}