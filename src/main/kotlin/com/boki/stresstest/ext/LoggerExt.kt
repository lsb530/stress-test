package com.boki.stresstest.ext

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

val Any.logger: KLogger
    get() = KotlinLogging.logger(this::class.qualifiedName!!)