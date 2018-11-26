package com.triangl.dashboard.projection

import com.triangl.dashboard.entity.Coordinate
import com.triangl.dashboard.entity.Router
import java.time.Instant
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
class TrackingPointCoordinateJoin {

    @Id
    var id: String? = null

    var trackedDeviceId: String? = null

    var timestamp: Instant? = null

    @Embedded
    var coordinate: Coordinate? = null
}