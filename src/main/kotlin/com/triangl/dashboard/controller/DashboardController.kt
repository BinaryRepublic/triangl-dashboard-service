package com.triangl.dashboard.controller

import com.triangl.dashboard.dto.VisitorAreaDurationReq
import com.triangl.dashboard.dto.VisitorByTimeAverageReq
import com.triangl.dashboard.dto.VisitorCountReq
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
    fun countVisitorsByTimeframe(@RequestBody visitorCountReqObj: VisitorCountReq): ResponseEntity<*> {

        val visitorCountResp =  dashboardService.countVisitorsByTimeframe(visitorCountReqObj)

        return ResponseEntity.ok().body(visitorCountResp)
    }

    @PostMapping("/areas/duration")
    fun getVisitorDurationByArea(@RequestBody visitorAreaDurationReqObj: VisitorAreaDurationReq): ResponseEntity<*> {

        val areaDwellTime = dashboardService.getVisitorsDurationByArea(visitorAreaDurationReqObj)

        return ResponseEntity.ok().body(areaDwellTime)
    }

    @PostMapping("/byTimeOfDay/average")
    fun getVisitorCountByTimeOfDayAverage(@RequestBody visitorByTimeAverageReqObj: VisitorByTimeAverageReq): ResponseEntity<*> {
        val visitorByTimeOfDayAverage = dashboardService.getVisitorCountByTimeOfDayAverage(visitorByTimeAverageReqObj)

        return ResponseEntity.ok().body(visitorByTimeOfDayAverage)
    }

}