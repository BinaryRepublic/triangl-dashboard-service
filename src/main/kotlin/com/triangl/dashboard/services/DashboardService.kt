package com.triangl.dashboard.services

import com.triangl.dashboard.dto.*
import com.triangl.dashboard.entity.TrackingPointCoordinateJoin
import com.triangl.dashboard.webservices.googleSQL.GoogleSQLWs
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Service
class DashboardService (
    val googleSQLWs: GoogleSQLWs,
    val weekDayCountService: WeekDayCountService
) {
    fun countVisitorsByTimeframe(visitorCountReqObj: VisitorCountReq): VisitorCountResp {
        val totalTimeFrame = Timeframe(LocalDateTime.parse(visitorCountReqObj.from), LocalDateTime.parse(visitorCountReqObj.to))
        val visitorCountResp = VisitorCountResp()

        val sliceSize = ((totalTimeFrame.from.until(totalTimeFrame.to, ChronoUnit.NANOS)) / visitorCountReqObj.dataPointCount)

        for (element: Int in 1..visitorCountReqObj.dataPointCount) {
            val newFrom = totalTimeFrame.from.plusNanos(sliceSize * (element - 1)).toString()
            val newTo = totalTimeFrame.from.plusNanos((sliceSize * element) - 1).toString()
            val newCount = googleSQLWs.countDistinctDeviceIdsInTimeFrame(visitorCountReqObj.customerId, newFrom, newTo)

            visitorCountResp.data.add(VisitorCountTimeframe(newFrom, newTo, newCount))
        }

        visitorCountResp.total = googleSQLWs.countDistinctDeviceIdsInTimeFrame(visitorCountReqObj.customerId, visitorCountReqObj.from, visitorCountReqObj.to)

        return visitorCountResp
    }

    fun getVisitorsDurationByArea(visitorAreaDurationReqObj: VisitorAreaDurationReq): List<Area> {
        val data = googleSQLWs.selectAllDeviceIdWithCoordinateInTimeframe(visitorAreaDurationReqObj.mapId, visitorAreaDurationReqObj.from, visitorAreaDurationReqObj.to)

        val respData = ArrayList<Area>()

        for (area in visitorAreaDurationReqObj.areas) {
            val areaTrackingPoints = data.filter { it.x!! in area.x..area.x2 && it.y!! in area.y..area.y2 }.sortedWith(compareBy({ it.trackedDeviceId }, {it.createdAt}))

            val dwellTime = calculateDwellTime(areaTrackingPoints)
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

            val rightTimeBorder = LocalDateTime.parse(lastInstant).plusSeconds(30).toString()

            if (point.trackedDeviceId != lastDeviceId
             || point.createdAt!! > rightTimeBorder) {

                dwellTime += LocalDateTime.parse(firstInstant).until(LocalDateTime.parse(lastInstant), ChronoUnit.SECONDS).toInt()
                dwellCount ++

                firstInstant = point.createdAt
            }

            lastInstant = point.createdAt
            lastDeviceId = point.trackedDeviceId
        }

        if (firstInstant != lastInstant) {
            dwellTime += LocalDateTime.parse(firstInstant).until(LocalDateTime.parse(lastInstant), ChronoUnit.SECONDS).toInt()
            dwellCount ++
        }

        return if (dwellCount > 0) {
            dwellTime / dwellCount
        } else {
            0
        }
    }

    fun getVisitorCountByTimeOfDayAverage(visitorByTimeAverageReqObj: VisitorByTimeAverageReq): ArrayList<VisitorByTimeAverageResp> {
        val data = googleSQLWs.selectAllDeviceIdInTimeframe(visitorByTimeAverageReqObj.customerId, visitorByTimeAverageReqObj.from, visitorByTimeAverageReqObj.to)
        val weekDays = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
        val response = ArrayList<VisitorByTimeAverageResp>()
        val countedWeekDays = weekDayCountService.countWeekdaysInTimeFrame(LocalDateTime.parse(visitorByTimeAverageReqObj.from), LocalDateTime.parse(visitorByTimeAverageReqObj.to))

        for (day in weekDays) {
            val visitorByTimeAverageResp = VisitorByTimeAverageResp(day.name.toLowerCase().capitalize())
            for (hour in 0..23) {
                val elements = data.filter { it.createdAt!!.dayOfWeek == day && it.createdAt!!.hour == hour }.groupBy { it.createdAt!!.dayOfYear }
                var totalVisitors = 0
                val totalDays = countedWeekDays.getOrDefault(day, 0)


                for ((_, value) in elements) {
                    val uniqueDeviceID = value.distinctBy { it.trackedDeviceId }
                    totalVisitors += uniqueDeviceID.size
                }

                val averageVisitors = if (totalDays != 0) {
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