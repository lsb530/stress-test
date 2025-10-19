package com.boki.stresstest.util

import java.security.SecureRandom

object SaltGenerator {
    private val random = SecureRandom()

    /**
     * 지정한 바이트 수 만큼 랜덤 바이트를 생성해
     * 16진수 문자열로 변환합니다.
     *
     * @param numBytes 생성할 바이트 수 (기본 8 → 16 hex chars)
     * @return 랜덤 hex salt 문자열
     */
    fun generateHexSalt(numBytes: Int = 8): String =
        ByteArray(numBytes).also { this.random.nextBytes(it) }
            .joinToString("") { "%02x".format(it) }
}