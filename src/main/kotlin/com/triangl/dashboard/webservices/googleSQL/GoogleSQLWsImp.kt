package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.entity.TrackingPointCoordinateJoin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service


@Service
@Profile("production")
class GoogleSQLWsImp: GoogleSQLWs {

    @Autowired
    var jdbcTemplate: JdbcTemplate? = null

    override fun countDistinctDeviceIdsInTimeFrame(customerId: String, start: String, end: String): Int {
        println("SELECT COUNT(DISTINCT(trackedDeviceId)) FROM TrackingPoint WHERE createdAt > $start AND createdAt < $end")
        return jdbcTemplate!!.queryForObject("SELECT COUNT(DISTINCT(trackedDeviceId)) FROM TrackingPoint WHERE createdAt > ? AND createdAt < ?", Long::class.java, start, end).toString().toInt()
    }

    override fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: String, end: String): List<TrackingPointCoordinateJoin> {
        return jdbcTemplate!!.query("SELECT TrackingPoint.trackedDeviceId, Coordinate.x, Coordinate.y, TrackingPoint.createdAt FROM TrackingPoint INNER JOIN Coordinate on TrackingPoint.coordinateId = Coordinate.id") {
            rs, _ -> TrackingPointCoordinateJoin().apply { trackedDeviceId = rs.getString("TrackingPoint.trackedDeviceId")
                                                           x = rs.getFloat("Coordinate.x")
                                                           y = rs.getFloat("Coordinate.y")
                                                           createdAt = rs.getTimestamp("createdAt").toLocalDateTime().toString() }
        }
    }

    override fun selectAllDeviceIdInTimeframe(mapId: String, start: String, end: String): List<TrackingPoint> {
        return jdbcTemplate!!.query("SELECT id, trackedDeviceId, coordinateId, createdAt FROM TrackingPoint WHERE createdAt > ? AND createdAt < ?", arrayOf(start, end)) {
            rs, _ -> TrackingPoint().apply { trackedDeviceId = rs.getString("trackedDeviceId")
                                    coordinateId = rs.getString("coordinateId")
                                    createdAt = rs.getTimestamp("createdAt").toLocalDateTime() }
        }
    }
}