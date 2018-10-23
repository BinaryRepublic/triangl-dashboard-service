package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.time.LocalDateTime

interface TrackingPointRepository: Repository<TrackingPoint, String> {

    @Query("SELECT COUNT(DISTINCT(trackedDeviceId)) FROM TrackingPoint WHERE timestamp > ?1 AND timestamp < ?2", nativeQuery = true)
    fun countDistinctOnTrackedDeviceIdByTimestampBetween(start: LocalDateTime, end: LocalDateTime): Int

    fun findByTimestampBetween(start: LocalDateTime, end: LocalDateTime): List<TrackingPoint>
}