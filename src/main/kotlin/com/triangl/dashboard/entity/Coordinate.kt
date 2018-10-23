package com.triangl.dashboard.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Coordinate")
open class Coordinate {
    @Id
    var id: String? = null

    var x: Float? = null

    var y: Float? = null
}