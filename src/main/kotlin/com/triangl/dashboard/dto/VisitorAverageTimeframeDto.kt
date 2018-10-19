package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class VisitorAverageTimeframeDto (
        @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
        val from: String,

        @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
        val to: String,

        val average: Double
)