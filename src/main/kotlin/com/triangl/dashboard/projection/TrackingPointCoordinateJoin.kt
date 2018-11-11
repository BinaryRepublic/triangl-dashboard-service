package com.triangl.dashboard.projection

import com.triangl.dashboard.entity.Coordinate
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "TrackingPoint")
class TrackingPointCoordinateJoin {
    @Id
    var id: String? = null

    @Column(name = "trackedDeviceId")
    var trackedDeviceId: String? = null

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coordinateId", nullable = false)
    var coordinate: Coordinate? = null

    var timestamp: Instant? = null
}