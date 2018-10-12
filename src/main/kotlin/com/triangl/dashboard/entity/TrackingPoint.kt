package com.triangl.dashboard.entity

import java.time.LocalDateTime
import javax.persistence.Basic


class TrackingPoint {
    var trackedDeviceId: String? = null

    var coordinateId: String? = null

    @Basic
    var createdAt: LocalDateTime? = null
}