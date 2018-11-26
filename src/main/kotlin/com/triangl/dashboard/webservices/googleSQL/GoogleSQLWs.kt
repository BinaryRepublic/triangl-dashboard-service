package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.Customer
import com.triangl.dashboard.projection.Manufacturer
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import java.time.Instant

interface GoogleSQLWs {

    fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: Instant, end: Instant): Int

    fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: Instant, end: Instant): List<TrackingPointCoordinateJoin>

    fun findCustomerById(customerId: String): Customer

    fun countManufactureAppearances(start: Instant, end: Instant): List<Manufacturer>
}