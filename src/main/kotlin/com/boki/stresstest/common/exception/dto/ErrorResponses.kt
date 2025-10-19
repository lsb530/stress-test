package com.boki.stresstest.common.exception.dto

import com.boki.stresstest.common.exception.code.ErrorCode

data class ErrorResponse(
    val code: String,
    val message: String?
) {
    constructor(errorCode: ErrorCode) : this(errorCode.code, errorCode.message)
}

data class FieldErrorResponse(
    val code: String,
    val errors: List<FieldErrorsBody>,
) {
    companion object {
        data class FieldErrorsBody(
            val fieldName: String,
            val requestValue: Any?,
            val message: String?,
        )
    }
}