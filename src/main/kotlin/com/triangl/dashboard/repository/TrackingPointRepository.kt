package com.triangl.dashboard.repository

import com.triangl.dashboard.entity.TrackingPoint
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.time.Instant

interface TrackingPointRepository: Repository<TrackingPoint, String> {

    @Query("SELECT COUNT(DISTINCT(trackedDeviceId)) FROM TrackingPoint WHERE timestamp BETWEEN ?1 AND ?2", nativeQuery = true)
    fun countDistinctOnTrackedDeviceIdByTimestampBetween(start: Instant, end: Instant): Int

    fun findByTimestampBetween(start: Instant, end: Instant): List<TrackingPoint>
}