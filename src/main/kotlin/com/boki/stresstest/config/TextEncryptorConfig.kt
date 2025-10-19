package com.boki.stresstest.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor

@Configuration
class TextEncryptorConfig(
    @Value("\${encrypt.secret}")
    val password: String,
    @Value("\${encrypt.salt}")
    val salt: String
) {
    @Bean
    fun textEncryptor(): TextEncryptor {
        // val hexSalt = generateHexSalt()
        return Encryptors.text(password, salt)
    }
}