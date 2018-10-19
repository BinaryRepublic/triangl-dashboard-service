package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class VisitorCountReqDto (
    var customerId: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    var from: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    var to: String,

    var dataPointCount: Int
)