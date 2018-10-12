package com.triangl.dashboard.controller

import com.triangl.dashboard.dto.VisitorAreaDurationReqDto
import com.triangl.dashboard.dto.VisitorByTimeAverageReqDto
import com.triangl.dashboard.dto.VisitorCountReqDto
import com.triangl.dashboard.services.DashboardService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/visitors", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class DashboardController (
    val dashboardService: DashboardService
) {
    @PostMapping("/count")
    fun countVisitorsByTimeframe(@RequestBody visitorCountReqDtoObj: VisitorCountReqDto): ResponseEntity<*> {

        val visitorCountResp =  dashboardService.countVisitorsByTimeframe(visitorCountReqDtoObj)

        return ResponseEntity.ok().body(visitorCountResp)
    }

    @PostMapping("/areas/duration")
    fun getVisitorDurationByArea(@RequestBody visitorAreaDurationReqDtoObj: VisitorAreaDurationReqDto): ResponseEntity<*> {

        val areaDwellTime = dashboardService.getVisitorsDurationByArea(visitorAreaDurationReqDtoObj)

        return ResponseEntity.ok().body(areaDwellTime)
    }

    @PostMapping("/byTimeOfDay/average")
    fun getVisitorCountByTimeOfDayAverage(@RequestBody visitorByTimeAverageReqDtoObj: VisitorByTimeAverageReqDto): ResponseEntity<*> {
        val visitorByTimeOfDayAverage = dashboardService.getVisitorCountByTimeOfDayAverage(visitorByTimeAverageReqDtoObj)

        return ResponseEntity.ok().body(visitorByTimeOfDayAverage)
    }

}