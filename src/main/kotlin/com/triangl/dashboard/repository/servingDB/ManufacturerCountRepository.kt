package com.triangl.dashboard.repository.servingDB

import com.triangl.dashboard.dbModels.servingDB.projection.ManufacturerCount
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.time.Instant

interface ManufacturerCountRepository: Repository<ManufacturerCount, String> {
    @Query("SELECT SUBSTRING(trackedDeviceId,1,8) AS manufactorId, COUNT(1) as count FROM TrackingPoint WHERE timestamp > ?1 AND timestamp < ?2 GROUP BY 1", nativeQuery = true)
    fun countManufactureAppearancesGroupBySubstring(start: Instant, end: Instant): List<ManufacturerCount>
}