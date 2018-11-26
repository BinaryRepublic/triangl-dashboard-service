package com.triangl.dashboard.repository

import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.Instant

interface TrackingPointCoordinateJoinRepository: CrudRepository<TrackingPointCoordinateJoin, String> {

    @Query(value = "SELECT t.id, t.timestamp, t.trackedDeviceId, c.x, c.y, c.areaId, t.createdAt, c.routerId FROM TrackingPoint t JOIN Coordinate c ON t.id = c.trackingPointId WHERE t.createdAt BETWEEN ?1 AND ?2", nativeQuery = true)
    fun findByTimestampBetween(start: Instant, end: Instant): List<TrackingPointCoordinateJoin>
}