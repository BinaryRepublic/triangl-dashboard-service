package com.triangl.dashboard.services

import com.triangl.dashboard.dto.*
import com.triangl.dashboard.helper.InstantHelper
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.webservices.googleSQL.GoogleSQLWs
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Service
class DashboardService (
    val googleSQLWs: GoogleSQLWs,
    val weekDayCountService: WeekDayCountService
) {
    fun visitorsDuringTimeframe(visitorCountReqDtoObj: VisitorCountReqDto): VisitorCountRespDto {
        val totalTimeFrame = TimeframeDto(visitorCountReqDtoObj.from, visitorCountReqDtoObj.to)
        val visitorCountResp = VisitorCountRespDto()

        val sliceSize = ((totalTimeFrame.from.until(totalTimeFrame.to, ChronoUnit.NANOS)) / visitorCountReqDtoObj.dataPointCount)

        for (index: Int in 0..(visitorCountReqDtoObj.dataPointCount - 1)) {
            val newFrom = totalTimeFrame.from.plusNanos(sliceSize * index)
            val newTo = totalTimeFrame.from.plusNanos((sliceSize * (index + 1)) - 1)
            val newCount = googleSQLWs.countDistinctDeviceIdsInTimeFrame(
                visitorCountReqDtoObj.customerId,
                newFrom,
                newTo
            )

            visitorCountResp.data.add(
                VisitorCountTimeframeDto(
                    newFrom.toString(),
                    newTo.toString(),
                    newCount
                )
            )
        }

        visitorCountResp.total = googleSQLWs.countDistinctDeviceIdsInTimeFrame(visitorCountReqDtoObj.customerId, visitorCountReqDtoObj.from, visitorCountReqDtoObj.to)

        return visitorCountResp
    }

    fun getVisitorsDurationByArea(visitorAreaDurationReqDtoObj: VisitorAreaDurationReqDto): List<AreaDto> {
        val data = googleSQLWs.selectAllDeviceIdWithCoordinateInTimeframe(visitorAreaDurationReqDtoObj.mapId, visitorAreaDurationReqDtoObj.from, visitorAreaDurationReqDtoObj.to)

        return visitorAreaDurationReqDtoObj.areaDtos.map { area ->
            val areaTrackingPoints = data.filter {
                it.coordinate!!.x!! in area.corner1!!.x..area.corner2!!.x
                && it.coordinate!!.y!! in area.corner1!!.y..area.corner2!!.y
            }.sortedWith(
                compareBy(
                    {it.trackedDeviceId},
                    {it.timestamp}
                )
            )
            area.customerCount = areaTrackingPoints.distinctBy { it.trackedDeviceId }.count()

            if (areaTrackingPoints.isNotEmpty()) {
                area.dwellTime = calculateDwellTime(areaTrackingPoints)
            } else {
                area.dwellTime = 0
            }
            area
        }
    }

    fun getVisitorsDurationByPolygon(visitorAreaDurationReqDtoObj: VisitorAreaDurationReqDto): List<AreaDto> {
        val data = googleSQLWs.selectAllDeviceIdWithCoordinateInTimeframe(visitorAreaDurationReqDtoObj.mapId, visitorAreaDurationReqDtoObj.from, visitorAreaDurationReqDtoObj.to)

        return visitorAreaDurationReqDtoObj.areaDtos.map {area ->
            val trackingPointsInPolygon = data.filter {
                area.corners!!.contains(it.coordinate!!.x!!.toInt(), it.coordinate!!.y!!.toInt())
            }.sortedWith(
                compareBy(
                    {it.trackedDeviceId},
                    {it.timestamp}
                )
            )
            area.customerCount = trackingPointsInPolygon.distinctBy { it.trackedDeviceId }.count()

            if (trackingPointsInPolygon.isNotEmpty()) {
                area.dwellTime = calculateDwellTime(trackingPointsInPolygon)
            } else {
                area.dwellTime = 0
            }
            area
        }
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
        val instantHelper = InstantHelper(ZoneOffset.of("+02:00"))
        val data = googleSQLWs.selectAllDeviceIdInTimeframe(
                visitorByTimeAverageReqDtoObj.customerId,
                instantHelper.toLocalDateTime(visitorByTimeAverageReqDtoObj.from),
                instantHelper.toLocalDateTime(visitorByTimeAverageReqDtoObj.to)
        )

        val weekDays = DayOfWeek.values()
        val response = ArrayList<VisitorByTimeAverageRespDto>()
        val countedWeekDays = weekDayCountService.occurrencesOfWeekDaysInTimeframe(
                instantHelper.toLocalDateTime(visitorByTimeAverageReqDtoObj.from).toLocalDate(),
                instantHelper.toLocalDateTime(visitorByTimeAverageReqDtoObj.to).toLocalDate()
        )

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

                visitorByTimeAverageResp.values.add(
                    VisitorAverageTimeframeDto(
                        from = LocalTime.of(hour,0).toString(),
                        to = LocalTime.of(hour,0).plusHours(1).toString(),
                        average = averageVisitors
                    )
                )
            }
            response.add(visitorByTimeAverageResp)
        }

        return response
    }
}