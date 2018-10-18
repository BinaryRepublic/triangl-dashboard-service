package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class VisitorCountTimeframeDto (
    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    val from: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    val to: String,

    @ApiModelProperty(notes = "Unique Customer Count")
    val count: Int
)