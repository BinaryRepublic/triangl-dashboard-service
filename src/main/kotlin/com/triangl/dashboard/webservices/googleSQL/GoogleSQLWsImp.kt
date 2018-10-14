package com.triangl.dashboard.webservices.googleSQL

import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
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
        return jdbcTemplate!!.queryForObject("SELECT COUNT(DISTINCT(trackedDeviceId)) FROM TrackingPoint WHERE timestamp > ? AND timestamp < ?", Long::class.java, start, end).toString().toInt()
    }

    override fun selectAllDeviceIdWithCoordinateInTimeframe(mapId: String, start: String, end: String): List<TrackingPointCoordinateJoin> {
        return jdbcTemplate!!.query("SELECT TrackingPoint.trackedDeviceId, Coordinate.x, Coordinate.y, TrackingPoint.timestamp FROM TrackingPoint INNER JOIN Coordinate on TrackingPoint.coordinateId = Coordinate.id") {
            rs, _ -> TrackingPointCoordinateJoin().apply { trackedDeviceId = rs.getString("TrackingPoint.trackedDeviceId")
                                                           x = rs.getFloat("Coordinate.x")
                                                           y = rs.getFloat("Coordinate.y")
                                                           createdAt = rs.getTimestamp("timestamp").toInstant().toString() }
        }
    }

    override fun selectAllDeviceIdInTimeframe(mapId: String, start: String, end: String): List<TrackingPoint> {
        return jdbcTemplate!!.query("SELECT id, trackedDeviceId, coordinateId, createdAt FROM TrackingPoint WHERE timestamp > ? AND timestamp < ?", arrayOf(start, end)) {
            rs, _ -> TrackingPoint().apply { trackedDeviceId = rs.getString("trackedDeviceId")
                                    coordinateId = rs.getString("coordinateId")
                                    createdAt = rs.getTimestamp("timestamp").toLocalDateTime() }
        }
    }
}