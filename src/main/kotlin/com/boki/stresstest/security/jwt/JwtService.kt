package com.boki.stresstest.security.jwt

import com.boki.stresstest.ext.logger
import com.boki.stresstest.security.role.UserRole
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.*
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.UUID
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(
    private val textEncryptor: TextEncryptor,
    private val objectMapper: ObjectMapper,

    @Value("\${jwt.secret-key}")
    val secretKey: String,
    @Value("\${jwt.access-token-expiration}")
    val accessTokenExpiration: Long,
    @Value("\${jwt.refresh-token-expiration}")
    val refreshTokenExpiration: Long,
    @Value("\${jwt.admin-access-token-expiration}")
    val adminAccessTokenExpiration: Long,
    @Value("\${jwt.admin-refresh-token-expiration}")
    val adminRefreshTokenExpiration: Long,
) {
    private val signingKey: SecretKeySpec
        get() {
            val keyBytes: ByteArray = Base64.getDecoder().decode(secretKey)
            return SecretKeySpec(keyBytes, 0, keyBytes.size, "HmacSHA256")
        }

    fun generateAccessToken(authentication: Authentication): String {
        val username = (authentication.principal as User).username
        val roles = authentication.authorities.map { SimpleGrantedAuthority(it?.authority) }.toList()

        val isAdmin = authentication.authorities
            .any { it.authority == "ROLE_ADMIN" }

        val expiration: Long = when {
            isAdmin -> adminAccessTokenExpiration
            else -> accessTokenExpiration
        }

        val claimsJson = objectMapper.writeValueAsString(mapOf("roles" to roles))
        val encryptedClaims = textEncryptor.encrypt(claimsJson)
        return buildBasicToken(username, expiration)
            .claim("data", encryptedClaims)
            .compact()
    }

    fun generateRefreshToken(authentication: Authentication): String {
        val username = (authentication.principal as User).username

        val isAdmin = authentication.authorities
            .any { it.authority == "ROLE_ADMIN" }

        val expiration: Long = when {
            isAdmin -> adminRefreshTokenExpiration
            else -> refreshTokenExpiration
        }

        return buildBasicToken(username, expiration)
            .compact()
    }

    fun parseUsername(token: String): String {
        return textEncryptor.decrypt(extractAllClaims(token).subject)
    }

    @Suppress("UNCHECKED_CAST")
    fun extractAuthorities(token: String): List<String> {
        val claimsMap = decryptClaims(token)

        val rolesList = claimsMap["roles"] as? List<Map<String, Any>>
            ?: return emptyList()

        return rolesList.mapNotNull { it["authority"] as? String }
    }

    fun extractUserRoles(token: String): List<UserRole> =
        extractAuthorities(token)
            .mapNotNull { ga ->
                ga.removePrefix("ROLE_")
                    .let { roleName ->
                        runCatching { UserRole.valueOf(roleName) }.getOrNull()
                    }
            }

    fun parseExpiry(token: String): Instant =
        extractAllClaims(token)
            .expiration.toInstant()

    fun parseJti(token: String): String =
        extractAllClaims(token)
            .id

    fun decryptClaims(token: String): Map<String, Any> {
        val claims = extractAllClaims(token)
        val encrypted = claims["data"] as? String
            ?: throw IllegalArgumentException("Encrypted data missing")
        val json = textEncryptor.decrypt(encrypted)
        return objectMapper.readValue<Map<String, Any>>(json)
    }

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload

    fun verifyToken(token: String) {
        try {
            extractAllClaims(token)
        } catch (e: ExpiredJwtException) {
            throw e
        } catch (e: JwtException) {
            val errMessage: String = when (e) {
                is SignatureException -> "서명이 잘못된 JWT 토큰입니다."
                is MalformedJwtException -> "잘못된 형식의 JWT 토큰입니다."
                is UnsupportedJwtException -> "지원하지 않는 JWT 토큰입니다."
                else -> e.message ?: "알 수 없는 JWT 오류"
            }
            logger.error { "JWT token verification failed: $errMessage" }
            throw JwtException(errMessage)
        }
    }

    private fun buildBasicToken(subject: String, expiration: Long): JwtBuilder {
        val encryptedSubject = textEncryptor.encrypt(subject)

        return Jwts.builder()
            .id(UUID.randomUUID().toString()) // jti claim
            .subject(encryptedSubject) // sub
            .issuedAt(Date(System.currentTimeMillis())) // iat
            .expiration(Date(Date().time + expiration * 1_000)) // exp
            .signWith(signingKey)
    }
}