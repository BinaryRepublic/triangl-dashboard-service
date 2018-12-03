package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.dbModels.servingDB.entity.Coordinate
import com.triangl.dashboard.dbModels.servingDB.entity.Customer
import com.triangl.dashboard.dbModels.servingDB.entity.TrackingPoint
import com.triangl.dashboard.dbModels.servingDB.projection.ManufacturerCount
import com.triangl.dashboard.dbModels.servingDB.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.dbModels.utilsDB.entity.MacManufacturer
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant

@Service
@Profile("test")
class GoogleSQLWsMock: GoogleSQLWs {

    val trackingPoint1 = TrackingPoint().apply { trackedDeviceId = "1"; timestamp = Instant.parse("2018-09-24T07:59:59.996Z") }
    val trackingPoint2 = TrackingPoint().apply { trackedDeviceId = "2"; timestamp = Instant.parse("2018-09-24T09:59:59.996Z") }
    val trackingPoint3 = TrackingPoint().apply { trackedDeviceId = "3"; timestamp = Instant.parse("2018-09-24T11:59:59.996Z") }

    val trackingPointCoordinateJoin1 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "1"; coordinate = Coordinate().apply { x = 1f; y = 2f; }; timestamp = Instant.parse("2018-09-24T07:59:30.996Z") }
    val trackingPointCoordinateJoin2 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "1"; coordinate = Coordinate().apply { x = 4f; y = 6f; }; timestamp = Instant.parse("2018-09-24T07:59:59.996Z") }
    val trackingPointCoordinateJoin3 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "2"; coordinate = Coordinate().apply { x = 15f; y = 11f; }; timestamp = Instant.parse("2018-09-24T07:59:40.996Z") }
    val trackingPointCoordinateJoin4 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "2"; coordinate = Coordinate().apply { x = 15f; y = 19f; }; timestamp = Instant.parse("2018-09-24T07:59:59.996Z") }
    val trackingPointCoordinateJoin5 = TrackingPointCoordinateJoin().apply { trackedDeviceId = "2"; coordinate = Coordinate().apply { x = 1f; y = 2f; }; timestamp = Instant.parse("2018-09-24T08:00:20.996Z") }

    val manufacturerCount1 = ManufacturerCount().apply { manufacturerId = "11:11:11"; count = 10 }
    val manufacturerCount2 = ManufacturerCount().apply { manufacturerId = "22:22:22"; count = 1 }
    val manufacturerCount3 = ManufacturerCount().apply { manufacturerId = "33:33:33"; count = 25 }

    val macManufacturer1 = MacManufacturer().apply { mac = "11:11:11"; companyName = "Apple" }
    val macManufacturer2 = MacManufacturer().apply { mac = "22:22:22"; companyName = "Samsung" }
    val macManufacturer3 = MacManufacturer().apply { mac = "33:33:33"; companyName = "One Plus" }

    override fun findCustomerById(customerId: String): Customer {
        return Customer().apply { id = customerId; name = customerId + "Name" }
    }

    override fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: Instant, end: Instant): Int {
        return listOf(trackingPoint1, trackingPoint2, trackingPoint3).size
    }

    override fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: Instant, end: Instant): List<TrackingPointCoordinateJoin> {
        return listOf(trackingPointCoordinateJoin1, trackingPointCoordinateJoin2, trackingPointCoordinateJoin3, trackingPointCoordinateJoin4, trackingPointCoordinateJoin5)
    }

    override fun countManufactureAppearances(start: Instant, end: Instant): List<ManufacturerCount> {
        return listOf(manufacturerCount1, manufacturerCount2, manufacturerCount3)
    }

    override fun getManufacturerNameForMacsInList(macs: List<String>): List<MacManufacturer> {
        return listOf(macManufacturer1, macManufacturer2, macManufacturer3)
    }
}
