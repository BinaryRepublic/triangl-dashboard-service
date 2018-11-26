package com.triangl.dashboard.repository

import com.triangl.dashboard.projection.Manufacturer
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.time.Instant

interface ManufacturerRepository: Repository<Manufacturer, String> {
    @Query("SELECT SUBSTRING(trackedDeviceId,1,8) AS manufactorId, COUNT(1) as count FROM TrackingPoint WHERE timestamp > ?1 AND timestamp < ?2 GROUP BY 1", nativeQuery = true)
    fun countManufactureAppearancesGroupBySubstring(start: Instant, end: Instant): List<Manufacturer>
}