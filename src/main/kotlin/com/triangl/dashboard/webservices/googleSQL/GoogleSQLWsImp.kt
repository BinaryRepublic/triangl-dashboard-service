package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.entity.TrackingPointCoordinateJoin
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("production")
class GoogleSQLWsImp: GoogleSQLWs {
    override fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: String, end: String): Int {
        return 3
    }

    override fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String): List<TrackingPointCoordinateJoin> {
        return listOf(TrackingPointCoordinateJoin())
    }

    override fun selectAllDeviceIdInTimeframe(mapId: String): List<TrackingPoint> {
        return listOf(TrackingPoint())
    }
}