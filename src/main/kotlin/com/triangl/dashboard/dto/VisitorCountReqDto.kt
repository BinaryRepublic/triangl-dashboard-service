package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant

class VisitorCountReqDto (
    var customerId: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var from: Instant,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var to: Instant,

    var dataPointCount: Int
)