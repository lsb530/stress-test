package com.boki.stresstest.security.jwt

import com.boki.stresstest.feat.admin.repository.AdminRepository
import com.boki.stresstest.security.dto.SecurityUser
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JwtAdminUserDetailsService(
    private val adminRepository: AdminRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val admin = adminRepository.findByIdOrNull(username.toLong())
            ?: throw UsernameNotFoundException("Admin not found: $username")

        return SecurityUser(admin.id!!, admin.role)
    }

}