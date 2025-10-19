package com.boki.stresstest.common.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.security.crypto.password.PasswordEncoder

@Converter
class PasswordConverter(
    private val passwordEncoder: PasswordEncoder
) : AttributeConverter<String, String> {
    override fun convertToDatabaseColumn(attribute: String): String {
        return passwordEncoder.encode(attribute)
    }

    override fun convertToEntityAttribute(dbData: String): String {
        return dbData
    }
}