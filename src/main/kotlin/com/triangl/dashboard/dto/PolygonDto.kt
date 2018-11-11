package com.triangl.dashboard.dto

import com.fasterxml.jackson.annotation.JsonValue
import java.awt.Polygon
import kotlin.math.roundToInt

class PolygonDto (
    @JsonValue
    private val corners: List<LocationDto>
): Polygon(
    corners.map { it.x.roundToInt() }.toIntArray(),
    corners.map { it.y.roundToInt() }.toIntArray(),
    corners.size
)