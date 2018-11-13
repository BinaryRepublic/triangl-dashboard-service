package com.triangl.dashboard.dto

import com.triangl.dashboard.entity.Coordinate
import io.swagger.annotations.ApiModelProperty

class AreaDto (

    var corner1: LocationDto? = null,

    var corner2: LocationDto? = null,

    var corners: PolygonDto? = null,

    @ApiModelProperty(notes = "Average Dwell Time in seconds")
    var dwellTime: Int? = null,

    @ApiModelProperty(notes = "Unique Customers in the timeframe in this area")
    var customerCount: Int? = null,

    var percentageOfAllVisitors: Float? = null
) {
    fun contains(location: Coordinate): Boolean {
        return if (corners != null) {
            return (corners!!.contains(location.x!!.toInt(), location.x!!.toInt()))
        } else if (corner1 != null && corner2 != null) {
            return (location.x!! in corner1!!.x..corner2!!.x && location.y!! in corner1!!.y..corner2!!.y)
        } else {
            false
        }
    }
}