package com.boki.stresstest.aop

import com.boki.stresstest.ext.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@Aspect
@Component
class LoggingAspect {
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    fun controllerPointCut() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    fun servicePointCut() {
    }

    @Pointcut("@within(org.springframework.stereotype.Repository)")
    fun repositoryPointCut() {
    }

    @Pointcut("controllerPointCut() || servicePointCut() || repositoryPointCut()")
    fun loggingPointcut() {
    }

    @OptIn(ExperimentalTime::class)
    @Around("loggingPointcut()")
    @Throws(Throwable::class)
    fun logAround(joinPoint: ProceedingJoinPoint): Any? {
        val methodName = joinPoint.signature.toShortString()

        val (resultOrError, duration) = measureTimedValue {
            runCatching { joinPoint.proceed() }
        }

        logger.debug { "Entering: $methodName" }

        resultOrError.fold(
            // 성공, 실패 모두 처리
            onSuccess = { result ->
                logger.debug { "Exiting: $methodName - Elapsed time: ${duration.inWholeMilliseconds}ms" }
                return result
            },
            onFailure = { t ->
                logger.debug { "Exiting with exception in $methodName - Elapsed time: ${duration.inWholeMilliseconds}ms: $t" }
                throw t
            }
        )
    }
}