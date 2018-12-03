package com.triangl.dashboard.dbModels.servingDB.projection

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "TrackingPoint")
class ManufacturerCount {
    @Id
    @Column(name = "manufactorId")
    var manufacturerId: String? = null

    var count: Int? = null
}