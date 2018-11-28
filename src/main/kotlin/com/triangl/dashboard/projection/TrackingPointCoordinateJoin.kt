package com.triangl.dashboard.projection

import com.triangl.dashboard.entity.Coordinate
import java.time.Instant
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class TrackingPointCoordinateJoin {

    @Id
    var id: String? = null

    var trackedDeviceId: String? = null

    var timestamp: Instant? = null

    @Embedded
    var coordinate: Coordinate? = null
}