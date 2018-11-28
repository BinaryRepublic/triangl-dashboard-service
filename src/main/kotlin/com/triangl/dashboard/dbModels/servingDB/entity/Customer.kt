package com.triangl.dashboard.dbModels.servingDB.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "Customer")
open class Customer {
    @Id
    var id: String? = null

    var name: String? = null

    @OneToMany(mappedBy = "customerId")
    var maps: Set<Map>? = null

    var lastUpdatedAt: String? = null

    var createdAt: String? = null
}