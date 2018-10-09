package com.triangl.dashboard.services

import com.triangl.dashboard.dto.*
import com.triangl.dashboard.entity.TrackingPointCoordinateJoin
import com.triangl.dashboard.webservices.googleSQL.GoogleSQLWs
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class DashboardService (
    val googleSQLWs: GoogleSQLWs,
    val weekDayCountService: WeekDayCountService
) {
    fun countVisitorsByTimeframe(visitorCountReqObj: VisitorCountReq): VisitorCountResp {
        var totalTimeFrame = Timeframe(Instant.parse(visitorCountReqObj.from), Instant.parse(visitorCountReqObj.to))
        var visitorCountResp = VisitorCountResp()

        var sliceSize = ((totalTimeFrame.to.toEpochMilli() - totalTimeFrame.from.toEpochMilli()) / visitorCountReqObj.dataPointCount).toInt()

        for (element: Int in 1..visitorCountReqObj.dataPointCount) {
            var newFrom = Instant.ofEpochMilli(totalTimeFrame.from.toEpochMilli() + (sliceSize * (element - 1))).toString()
            var newTo = Instant.ofEpochMilli(totalTimeFrame.from.toEpochMilli() + (sliceSize * element) - 1).toString()
            var newCount = googleSQLWs.countDistinctDeviceIdsInTimeFrame(visitorCountReqObj.customerId, newFrom, newTo)

            visitorCountResp.data.add(VisitorCountTimeframe(newFrom, newTo, newCount))
        }

        visitorCountResp.total = googleSQLWs.countDistinctDeviceIdsInTimeFrame(visitorCountReqObj.customerId, visitorCountReqObj.from, visitorCountReqObj.to)

        return visitorCountResp
    }

    fun getVisitorsDurationByArea(visitorAreaDurationReqObj: VisitorAreaDurationReq): List<Area> {
        val data = googleSQLWs.selectAllDeviceIdWithCoordinateInTimeframe(visitorAreaDurationReqObj.mapId)

        val respData = ArrayList<Area>()

        for (area in visitorAreaDurationReqObj.areas) {
            var areaTrackingPoints = data.filter { it.x!! in area.x..area.x2 && it.y!! in area.y..area.y2 }.sortedWith(compareBy({ it.trackedDeviceId }, {it.createdAt}))

            var dwellTime = calculateDwellTime(areaTrackingPoints)
            area.dwellTime = dwellTime
            respData.add(area)
        }

        return respData
    }

    fun calculateDwellTime(areaTrackingPoints: List<TrackingPointCoordinateJoin>): Int {
        var dwellTime = 0
        var dwellCount = 0

        var firstInstant = areaTrackingPoints[0].createdAt
        var lastInstant = areaTrackingPoints[0].createdAt
        var lastDeviceId = areaTrackingPoints[0].trackedDeviceId

        for (point in areaTrackingPoints) {

            val rightTimeBorder = Instant.parse(lastInstant).plusSeconds(30).toString()

            if (point.trackedDeviceId != lastDeviceId
             || point.createdAt!! > rightTimeBorder) {

                dwellTime += (Instant.parse(lastInstant).epochSecond - Instant.parse(firstInstant).epochSecond).toInt()
                dwellCount ++

                firstInstant = point.createdAt
            }

            lastInstant = point.createdAt
            lastDeviceId = point.trackedDeviceId
        }

        if (firstInstant != lastInstant) {
            dwellTime += (Instant.parse(lastInstant).epochSecond - Instant.parse(firstInstant).epochSecond).toInt()
            dwellCount ++
        }

        return if (dwellCount > 0) {
            dwellTime / dwellCount
        } else {
            0
        }
    }

    fun getVisitorCountByTimeOfDayAverage(visitorByTimeAverageReqObj: VisitorByTimeAverageReq): ArrayList<VisitorByTimeAverageResp> {
        val data = googleSQLWs.selectAllDeviceIdInTimeframe(visitorByTimeAverageReqObj.customerId)
        val weekDays = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
        val response = ArrayList<VisitorByTimeAverageResp>()
        val countedWeekDays = weekDayCountService.countWeekdaysInTimeFrame(LocalDateTime.parse(visitorByTimeAverageReqObj.from), LocalDateTime.parse(visitorByTimeAverageReqObj.to))

        for (day in weekDays) {
            var visitorByTimeAverageResp = VisitorByTimeAverageResp(day.name.toLowerCase().capitalize())
            for (hour in 0..23) {
                var elements = data.filter { it.createdAt!!.dayOfWeek == day && it.createdAt!!.hour == hour }.groupBy { it.createdAt!!.dayOfYear }
                var totalVisitors = 0
                var totalDays = countedWeekDays.getOrDefault(day, 0)


                for ((key, value) in elements) {
                    var uniqueDeviceID = value.distinctBy { it.trackedDeviceId }
                    totalVisitors += uniqueDeviceID.size
                }

                var averageVisitors = if (totalDays != 0) {
                    totalVisitors / totalDays.toDouble()
                } else {
                    0.0
                }

                visitorByTimeAverageResp.values.add(VisitorAverageTimeframe(LocalTime.of(hour,0).toString(), LocalTime.of(hour,0).plusHours(1).toString(), averageVisitors))
            }
            response.add(visitorByTimeAverageResp)
        }

        return response
    }
}