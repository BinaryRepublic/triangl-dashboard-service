package com.triangl.dashboard.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@Entity
@Table(name = "Coordinate")
open class Coordinate {
    @Id
    var id: String? = null

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "routerId")
    open var router: Router? = null

    var areaId: String? = null

    var x: Float? = null

    var y: Float? = null
}