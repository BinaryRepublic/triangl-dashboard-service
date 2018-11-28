package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.dbModels.servingDB.entity.Customer
import com.triangl.dashboard.dbModels.servingDB.projection.ManufacturerCount
import com.triangl.dashboard.dbModels.servingDB.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.dbModels.utilsDB.entity.MacManufacturer
import com.triangl.dashboard.repository.servingDB.CustomerRepository
import com.triangl.dashboard.repository.servingDB.ManufacturerCountRepository
import com.triangl.dashboard.repository.servingDB.TrackingPointCoordinateJoinRepository
import com.triangl.dashboard.repository.servingDB.TrackingPointRepository
import com.triangl.dashboard.repository.utilsDB.ManufacturerRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant


@Service
@Profile("production")
class GoogleSQLWsImp (
        val trackingPointRepository: TrackingPointRepository,
        val trackingPointCoordinateJoinRepository: TrackingPointCoordinateJoinRepository,
        val customerRepository: CustomerRepository,
        val manufacturerCountRepository: ManufacturerCountRepository,
        val macManufacturerRepository: ManufacturerRepository
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

    override fun countManufactureAppearances(start: Instant, end: Instant): List<ManufacturerCount> {
        return manufacturerCountRepository.countManufactureAppearancesGroupBySubstring(
            start = start,
            end = end
        )
    }

    override fun getManufacturerNameForMacsInList(macs: List<String>): List<MacManufacturer> {
        return macManufacturerRepository.findByMacIn(
            macs = macs
        )
    }
}