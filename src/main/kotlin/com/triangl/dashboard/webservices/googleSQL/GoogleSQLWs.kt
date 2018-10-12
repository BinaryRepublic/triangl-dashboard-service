package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin

interface GoogleSQLWs {
    fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: String, end: String): Int
    fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: String, end: String): List<TrackingPointCoordinateJoin>
    fun selectAllDeviceIdInTimeframe(mapId: String, start: String, end: String): List<TrackingPoint>
}