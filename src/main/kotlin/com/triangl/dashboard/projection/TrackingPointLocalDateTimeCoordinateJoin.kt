package com.triangl.dashboard.projection

import com.triangl.dashboard.entity.Coordinate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TrackingPoint")
class TrackingPointLocalDateTimeCoordinateJoin {
    @Id
    var id: String? = null

    @Column(name = "trackedDeviceId")
    var trackedDeviceId: String? = null

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coordinateId", nullable = false)
    var coordinate: Coordinate? = null

    var timestamp: LocalDateTime? = null
}