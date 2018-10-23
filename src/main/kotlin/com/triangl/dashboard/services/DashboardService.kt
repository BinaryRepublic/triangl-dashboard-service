package com.triangl.dashboard.services

import com.triangl.dashboard.dto.*
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.webservices.googleSQL.GoogleSQLWs
import org.springframework.stereotype.Service
import java.time.*
import java.time.temporal.ChronoUnit

@Service
class DashboardService (
    val googleSQLWs: GoogleSQLWs,
    val weekDayCountService: WeekDayCountService
) {
    fun visitorsDuringTimeframe(visitorCountReqDtoObj: VisitorCountReqDto): VisitorCountRespDto {
        val totalTimeFrame = TimeframeDto(Instant.parse(visitorCountReqDtoObj.from), Instant.parse(visitorCountReqDtoObj.to))
        val visitorCountResp = VisitorCountRespDto()

        val sliceSize = ((totalTimeFrame.from.until(totalTimeFrame.to, ChronoUnit.NANOS)) / visitorCountReqDtoObj.dataPointCount)

        for (index: Int in 0..visitorCountReqDtoObj.dataPointCount) {
            val newFrom = totalTimeFrame.from.plusNanos(sliceSize * index).toString()
            val newTo = totalTimeFrame.from.plusNanos((sliceSize * (index + 1)) - 1).toString()
            val newCount = googleSQLWs.countDistinctDeviceIdsInTimeFrame(visitorCountReqDtoObj.customerId, newFrom, newTo)


            visitorCountResp.data.add(VisitorCountTimeframeDto(newFrom, newTo, newCount))
        }

        visitorCountResp.total = googleSQLWs.countDistinctDeviceIdsInTimeFrame(visitorCountReqDtoObj.customerId, visitorCountReqDtoObj.from, visitorCountReqDtoObj.to)

        return visitorCountResp
    }

    fun getVisitorsDurationByArea(visitorAreaDurationReqDtoObj: VisitorAreaDurationReqDto): List<AreaDto> {
        val data = googleSQLWs.selectAllDeviceIdWithCoordinateInTimeframe(visitorAreaDurationReqDtoObj.mapId, visitorAreaDurationReqDtoObj.from, visitorAreaDurationReqDtoObj.to)

        val respData = ArrayList<AreaDto>()

        for (area in visitorAreaDurationReqDtoObj.areaDtos) {
            val areaTrackingPoints = data.filter { it.coordinate!!.x!! in area.corner1.x..area.corner2.x && it.coordinate!!.y!! in area.corner1.y..area.corner2.y }.sortedWith(compareBy({ it.trackedDeviceId }, {it.timestamp}))
            area.customerCount = areaTrackingPoints.distinctBy { it.trackedDeviceId }.count()

            if (areaTrackingPoints.isNotEmpty()) {
                area.dwellTime = calculateDwellTime(areaTrackingPoints)
            } else {
                area.dwellTime = 0
            }
            respData.add(area)
        }

        return respData
    }

    fun calculateDwellTime(areaTrackingPoints: List<TrackingPointCoordinateJoin>): Int {
        var dwellTime = 0
        var dwellCount = 0

        var firstInstant = areaTrackingPoints[0].timestamp
        var lastInstant = areaTrackingPoints[0].timestamp
        var lastDeviceId = areaTrackingPoints[0].trackedDeviceId

        for (point in areaTrackingPoints) {

            val rightTimeBorder = lastInstant!!.plusSeconds(30)

            if (point.trackedDeviceId != lastDeviceId
             || point.timestamp!! > rightTimeBorder) {

                dwellTime += firstInstant!!.until(lastInstant, ChronoUnit.SECONDS).toInt()
                dwellCount ++

                firstInstant = point.timestamp
            }

            lastInstant = point.timestamp
            lastDeviceId = point.trackedDeviceId
        }

        if (firstInstant != lastInstant) {
            dwellTime += firstInstant!!.until(lastInstant, ChronoUnit.SECONDS).toInt()
            dwellCount ++
        }

        return if (dwellCount > 0) {
            dwellTime / dwellCount
        } else {
            0
        }
    }

    fun getVisitorCountByTimeOfDayAverage(visitorByTimeAverageReqDtoObj: VisitorByTimeAverageReqDto): ArrayList<VisitorByTimeAverageRespDto> {
        val data = googleSQLWs.selectAllDeviceIdInTimeframe(visitorByTimeAverageReqDtoObj.customerId, visitorByTimeAverageReqDtoObj.from, visitorByTimeAverageReqDtoObj.to)
        val weekDays = DayOfWeek.values()
        val response = ArrayList<VisitorByTimeAverageRespDto>()
        val countedWeekDays = weekDayCountService.occurrencesOfWeekDaysInTimeframe(LocalDateTime.ofInstant(Instant.parse(visitorByTimeAverageReqDtoObj.from), ZoneOffset.UTC),
                                                                                                     LocalDateTime.ofInstant(Instant.parse(visitorByTimeAverageReqDtoObj.to), ZoneOffset.UTC))

        for (day in weekDays) {
            val visitorByTimeAverageResp = VisitorByTimeAverageRespDto(day.name.toLowerCase().capitalize())
            for (hour in 0..23) {
                val elements = data.filter { it.timestamp!!.dayOfWeek == day && it.timestamp!!.hour == hour }.groupBy { it.timestamp!!.dayOfYear }
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

                visitorByTimeAverageResp.values.add(VisitorAverageTimeframeDto(LocalTime.of(hour,0).toString(), LocalTime.of(hour,0).plusHours(1).toString(), averageVisitors))
            }
            response.add(visitorByTimeAverageResp)
        }

        return response
    }
}