package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.Customer
import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.Manufacturer
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.repository.*
import com.triangl.dashboard.repository.CustomerRepository
import com.triangl.dashboard.repository.TrackingPointCoordinateJoinRepository
import com.triangl.dashboard.repository.TrackingPointRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant


@Service
@Profile("production")
class GoogleSQLWsImp (
        val trackingPointRepository: TrackingPointRepository,
        val trackingPointCoordinateJoinRepository: TrackingPointCoordinateJoinRepository,
        val customerRepository: CustomerRepository,
        val manufacturerRepository: ManufacturerRepository
): GoogleSQLWs {

    override fun findCustomerById(customerId: String): Customer {
        return customerRepository.findById(
            customerId
        )
    }

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

    override fun countManufactureAppearances(start: Instant, end: Instant): List<Manufacturer> {
        return manufacturerRepository.countManufactureAppearancesGroupBySubstring(
            start = start,
            end = end
        )
    }
}