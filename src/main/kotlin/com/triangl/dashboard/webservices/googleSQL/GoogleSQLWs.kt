package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.projection.TrackingPointLocalDateTimeCoordinateJoin
import java.time.Instant
import java.time.LocalDateTime

interface GoogleSQLWs {
    fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: Instant, end: Instant): Int
    fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: Instant, end: Instant): List<TrackingPointCoordinateJoin>
    fun selectAllDeviceIdInTimeframe(mapId: String, start: Instant, end: Instant): List<TrackingPoint>
    fun selectAllDeviceIdWithCoordinateInTimeframeInLocalDateTime(mapId: String, start: LocalDateTime, end: LocalDateTime): List<TrackingPointLocalDateTimeCoordinateJoin>
}