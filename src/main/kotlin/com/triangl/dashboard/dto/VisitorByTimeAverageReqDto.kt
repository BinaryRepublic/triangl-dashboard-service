package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class VisitorByTimeAverageReqDto (
    val customerId: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    val from: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    val to: String
)