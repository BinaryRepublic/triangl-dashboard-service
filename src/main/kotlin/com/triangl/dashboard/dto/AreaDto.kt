package com.triangl.dashboard.dto

import io.swagger.annotations.ApiModelProperty

class AreaDto (

    var corner1: LocationDto? = null,

    var corner2: LocationDto? = null,

    var corners: PolygonDto? = null,

    @ApiModelProperty(notes = "Average Dwell Time in seconds")
    var dwellTime: Int? = null,

    @ApiModelProperty(notes = "Unique Customers in the timeframe in this area")
    var customerCount: Int? = null
)