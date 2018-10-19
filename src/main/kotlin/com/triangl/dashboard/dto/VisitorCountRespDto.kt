package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class VisitorCountRespDto (
        var data: ArrayList<VisitorCountTimeframeDto> = ArrayList(),

        @ApiModelProperty(notes = "Total Customer Count")
        var total: Int = 0
)