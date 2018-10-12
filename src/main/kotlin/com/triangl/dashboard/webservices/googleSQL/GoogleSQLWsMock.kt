package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Profile("test")
class GoogleSQLWsMock: GoogleSQLWs {
    val trackingPoint1 = TrackingPoint().apply { trackedDeviceId = "1"; coordinateId = "cId1"; createdAt = LocalDateTime.parse("2018-09-24T07:59:59.996") }
    val trackingPoint2 = TrackingPoint().apply { trackedDeviceId = "2"; coordinateId = "cId2"; createdAt = LocalDateTime.parse("2018-09-24T09:59:59.996") }
    val trackingPoint3 = TrackingPoint().apply { trackedDeviceId = "3"; coordinateId = "cId3"; createdAt = LocalDateTime.parse("2018-09-24T11:59:59.996") }

    val trackingPointCoordinateJoin1 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "1"; x = 1f; y = 2f; createdAt = "2018-09-24T07:59:30.996" }
    val trackingPointCoordinateJoin2 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "1"; x = 4f; y = 6f; createdAt = "2018-09-24T07:59:59.996" }
    val trackingPointCoordinateJoin3 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "2"; x = 15f; y = 11f; createdAt = "2018-09-24T07:59:40.996" }
    val trackingPointCoordinateJoin4 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "2"; x = 15f; y = 19f; createdAt = "2018-09-24T07:59:59.996" }
    val trackingPointCoordinateJoin5 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "2"; x = 1f; y = 2f; createdAt = "2018-09-24T08:00:20.996" }

    override fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: String, end: String): Int {
        return listOf(trackingPoint1, trackingPoint2, trackingPoint3).size
    }

    override fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: String, end: String): List<TrackingPointCoordinateJoin> {
        return listOf(trackingPointCoordinateJoin1, trackingPointCoordinateJoin2, trackingPointCoordinateJoin3, trackingPointCoordinateJoin4, trackingPointCoordinateJoin5)
    }

    override fun selectAllDeviceIdInTimeframe(mapId: String, start: String, end: String): List<TrackingPoint> {
        return listOf(trackingPoint1, trackingPoint2, trackingPoint3)
    }
}