package com.triangl.dashboard.support

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class InstantHelper (
    private val zoneOffset: ZoneId = ZoneId.of("Europe/Berlin")
) {
    fun toLocalDateTime(instant: Instant): LocalDateTime {
        return LocalDateTime.ofInstant(instant, zoneOffset)
    }
}