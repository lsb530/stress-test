package com.boki.stresstest.common.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActiveUserFilter(val value: Boolean = true)