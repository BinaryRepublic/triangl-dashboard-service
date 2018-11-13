package com.triangl.dashboard.controller

import com.triangl.dashboard.dto.*
import com.triangl.dashboard.services.DashboardService
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/visitors", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class DashboardController (
    val dashboardService: DashboardService
) {
    @ApiOperation(value = "Get the amount of unique Visitors in the requested timeframe per timeslice whereby timeslice is timeframe / dataPointCount", response = VisitorCountRespDto::class)
    @PostMapping("/count")
    fun countVisitorsByTimeframe(@RequestBody visitorCountReqDtoObj: VisitorCountReqDto): ResponseEntity<*> {

        val visitorCountResp =  dashboardService.visitorsDuringTimeframe(visitorCountReqDtoObj)

        return ResponseEntity.ok().body(visitorCountResp)
    }

    @ApiOperation(value = "Get average dwell time for every requested area", response = AreaDto::class, responseContainer = "List")
    @PostMapping("/areas/duration")
    fun getVisitorDurationByArea(@RequestBody visitorAreaDurationReqDtoObj: VisitorAreaDurationReqDto): ResponseEntity<*> {

        val areaDwellTime = dashboardService.getVisitorsDurationByArea(visitorAreaDurationReqDtoObj)

        return ResponseEntity.ok().body(areaDwellTime)
    }

    @ApiOperation(value = "Get average Visitor Count by time of Day and by Weekday", response = VisitorByTimeAverageRespDto::class)
    @PostMapping("/byTimeOfDay/average")
    fun getVisitorCountByTimeOfDayAverage(@RequestBody visitorByTimeAverageReqDtoObj: VisitorByTimeAverageReqDto): ResponseEntity<*> {
        val visitorByTimeOfDayAverage = dashboardService.getVisitorCountByTimeOfDayAverage(visitorByTimeAverageReqDtoObj)

        return ResponseEntity.ok().body(visitorByTimeOfDayAverage)
    }

    @ApiOperation(value = "Get percentage of all Visitors who were in the given areas in the given timeframe", response = AreaDto::class, responseContainer = "List")
    @PostMapping("/areas/percentage")
    fun getHowManyPercentOfVisitorsVisitedGivenArea(@RequestBody visitorAreaDurationReqDto: VisitorAreaDurationReqDto): ResponseEntity<*> {
        val visitorPercentageRespDto = dashboardService.getHowManyPercentOfVisitorsVisitedGivenArea(visitorAreaDurationReqDto)

        return ResponseEntity.ok().body(visitorPercentageRespDto)
    }

}