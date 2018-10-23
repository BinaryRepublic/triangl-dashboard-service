package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


@Service
@Profile("production")
class GoogleSQLWsImp (
    val trackingPointRepository: TrackingPointRepository,
    val trackingPointCoordinateJoinRepository: TrackingPointCoordinateJoinRepository
): GoogleSQLWs {

    override fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: String, end: String): Int {
        return trackingPointRepository.countDistinctOnTrackedDeviceIdByTimestampBetween(
                start = LocalDateTime.ofInstant(Instant.parse(start), ZoneOffset.UTC),
                end = LocalDateTime.ofInstant(Instant.parse(end), ZoneOffset.UTC)
        )
    }

    override fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: String, end: String): List<TrackingPointCoordinateJoin> {
        return trackingPointCoordinateJoinRepository.findByTimestampBetween(
                start = Instant.parse(start),
                end = Instant.parse(end)
        )
    }

    override fun selectAllDeviceIdInTimeframe(mapId: String, start: String, end: String): List<TrackingPoint> {
        return trackingPointRepository.findByTimestampBetween(
            start = LocalDateTime.ofInstant(Instant.parse(start), ZoneOffset.UTC),
            end = LocalDateTime.ofInstant(Instant.parse(end), ZoneOffset.UTC)
        )
    }
}