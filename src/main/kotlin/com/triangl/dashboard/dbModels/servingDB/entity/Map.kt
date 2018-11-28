package com.triangl.dashboard.dbModels.servingDB.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "Map")
class Map {
    @Id
    var id: String? = null

    var customerId: String? = null

    var name: String? = null

    var svgPath: String? = null

    var width: Float? = null

    var height: Float? = null

    @OneToMany(mappedBy = "mapId")
    var areas: Set<Area>? = null

    @OneToMany(mappedBy = "mapId")
    var router: Set<Router>? = null

    var lastUpdatedAt: String? = null

    var createdAt: String? = null
}