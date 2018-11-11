package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime


@Service
@Profile("production")
class GoogleSQLWsImp (
    val trackingPointRepository: TrackingPointRepository,
    val trackingPointCoordinateJoinRepository: TrackingPointCoordinateJoinRepository
): GoogleSQLWs {

    override fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: Instant, end: Instant): Int {
        return trackingPointRepository.countDistinctOnTrackedDeviceIdByTimestampBetween(
                start = start,
                end = end
        )
    }

    override fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: Instant, end: Instant): List<TrackingPointCoordinateJoin> {
        return trackingPointCoordinateJoinRepository.findByTimestampBetween(
                start = start,
                end = end
        )
    }

    override fun selectAllDeviceIdInTimeframe(mapId: String, start: LocalDateTime, end: LocalDateTime): List<TrackingPoint> {
        return trackingPointRepository.findByTimestampBetween(
            start = start,
            end = end
        )
    }
}