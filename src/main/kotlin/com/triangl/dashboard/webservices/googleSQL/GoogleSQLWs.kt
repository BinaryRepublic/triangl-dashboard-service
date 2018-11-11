package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import java.time.Instant
import java.time.LocalDateTime

interface GoogleSQLWs {
    fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: Instant, end: Instant): Int
    fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: Instant, end: Instant): List<TrackingPointCoordinateJoin>
    fun selectAllDeviceIdInTimeframe(mapId: String, start: LocalDateTime, end: LocalDateTime): List<TrackingPoint>
}