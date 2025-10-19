package com.boki.stresstest.security.jwt

import com.boki.stresstest.feat.user.repository.UserRepository
import com.boki.stresstest.security.dto.SecurityUser
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByIdOrNull(username.toLong())
            ?: throw UsernameNotFoundException("User not found: $username")

        return SecurityUser(user.id!!, user.role)
    }

}