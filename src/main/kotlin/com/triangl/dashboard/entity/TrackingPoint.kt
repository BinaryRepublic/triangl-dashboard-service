package com.triangl.dashboard.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "TrackingPoint")
open class TrackingPoint {
    @Id
    var id: String? = null

    @Column(name = "trackedDeviceId")
    var trackedDeviceId: String? = null

    var coordinateId: String? = null

    var timestamp: LocalDateTime? = null
}