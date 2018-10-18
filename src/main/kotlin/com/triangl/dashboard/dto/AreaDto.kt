package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class AreaDto (

    var corner1: LocationDto,

    var corner2: LocationDto,

    @ApiModelProperty(notes = "Average Dwell Time in seconds")
    var dwellTime: Int? = null
)