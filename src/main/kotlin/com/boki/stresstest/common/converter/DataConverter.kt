package com.boki.stresstest.common.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.security.crypto.encrypt.TextEncryptor

@Converter
class DataConverter(
    private val textEncryptor: TextEncryptor,
) : AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String?): String? {
        return attribute?.let { textEncryptor.encrypt(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        return dbData?.let { textEncryptor.decrypt(it) }
    }

}