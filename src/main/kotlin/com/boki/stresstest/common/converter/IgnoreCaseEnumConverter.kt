package com.boki.stresstest.common.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.springframework.stereotype.Component

@Component
class IgnoreCaseEnumConverter : ConverterFactory<String, Enum<*>> {

    override fun <T : Enum<*>?> getConverter(targetType: Class<T?>): Converter<String?, T?> {
        return Converter { source ->
            targetType.enumConstants.firstOrNull { it?.name.equals(source, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown ${targetType.simpleName}: $source")
        }
    }

}