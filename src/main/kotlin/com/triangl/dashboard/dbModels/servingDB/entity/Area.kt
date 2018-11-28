package com.triangl.dashboard.dbModels.servingDB.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "Area")
class Area {
    @Id
    var id: String? = null

    var mapId: String? = null

    @OneToMany(mappedBy = "areaId", targetEntity = Coordinate::class)
    var vertices: Set<Coordinate>? = null

    var createdAt: String? = null

    var lastUpdatedAt: String? = null
}