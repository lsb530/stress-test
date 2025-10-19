package com.boki.stresstest.common.doc

import com.boki.stresstest.common.exception.dto.ErrorResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

private const val COMMON_FAIL = """
    {
        "code": "Error Code",
        "message": "Error Message"
    }
    """

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponses(
    ApiResponse(
        responseCode = "4XX",
        description = "Client Common Error",
        content = [
            Content(
                schema = Schema(implementation = ErrorResponse::class),
                examples = [ExampleObject(value = COMMON_FAIL)
                ]
            ),
        ],
    ),
    ApiResponse(
        responseCode = "5XX",
        description = "Server Common Error",
        content = [
            Content(
                schema = Schema(implementation = ErrorResponse::class),
                examples = [ExampleObject(value = COMMON_FAIL)
                ]
            ),
        ],
    ),
)
annotation class CommonApiResponseSpec()