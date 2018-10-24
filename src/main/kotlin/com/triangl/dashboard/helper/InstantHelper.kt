package com.triangl.dashboard.helper

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class InstantHelper (
    var zoneOffset: ZoneId = ZoneId.of("Europe/Berlin")
) {
    fun toLocalDateTime(instant: Instant): LocalDateTime {
        return LocalDateTime.ofInstant(instant, zoneOffset)
    }
}