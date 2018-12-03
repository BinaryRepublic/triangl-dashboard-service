package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.dbModels.servingDB.entity.Customer
import com.triangl.dashboard.dbModels.servingDB.projection.ManufacturerCount
import com.triangl.dashboard.dbModels.servingDB.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.dbModels.utilsDB.entity.MacManufacturer
import java.time.Instant

interface GoogleSQLWs {

    fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: Instant, end: Instant): Int

    fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: Instant, end: Instant): List<TrackingPointCoordinateJoin>

    fun findCustomerById(customerId: String): Customer

    fun countManufactureAppearances(start: Instant, end: Instant): List<ManufacturerCount>

    fun getManufacturerNameForMacsInList(macs: List<String>): List<MacManufacturer>
}