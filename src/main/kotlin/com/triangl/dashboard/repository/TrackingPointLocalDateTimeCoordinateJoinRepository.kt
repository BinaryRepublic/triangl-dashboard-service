package com.triangl.dashboard.repository

import com.triangl.dashboard.projection.TrackingPointLocalDateTimeCoordinateJoin
import org.springframework.data.repository.Repository
import java.time.LocalDateTime

interface TrackingPointLocalDateTimeCoordinateJoinRepository: Repository<TrackingPointLocalDateTimeCoordinateJoin, String> {

    fun findByTimestampBetween(start: LocalDateTime, end: LocalDateTime): List<TrackingPointLocalDateTimeCoordinateJoin>
}