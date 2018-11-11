package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import org.springframework.data.repository.CrudRepository
import java.time.Instant

interface TrackingPointCoordinateJoinRepository: CrudRepository<TrackingPointCoordinateJoin, String> {
    fun findByTimestampBetween(start: Instant, end: Instant): List<TrackingPointCoordinateJoin>
}