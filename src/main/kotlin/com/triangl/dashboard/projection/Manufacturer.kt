package com.triangl.dashboard.projection

import javax.persistence.*

@Entity
@Table(name = "TrackingPoint")
class Manufacturer {
    @Id
    @Column(name = "manufactorId")
    var manufactorId: String? = null

    var count: Int? = null

    @Transient
    var percent: Float? = null
}