package com.triangl.dashboard.entity

import javax.persistence.*

@Entity
@Table(name = "Router")
class Router {
    @Id
    var id: String? = null

    var mapId: String? = null

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coordinateId", nullable = false)
    var location: Coordinate? = null

    var lastUpdatedAt: String? = null

    var createdAt: String? = null
}