package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class VisitorAreaDurationReqDto (
    var mapId: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    var from: String,

    @ApiModelProperty(notes = "TimestampString in GMT+00:00 format")
    var to: String,

    var areaDtos: List<AreaDto>
)