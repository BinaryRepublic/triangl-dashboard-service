package com.triangl.dashboard.dbModels.utilsDB.entity

import javax.persistence.*

@Entity
@Table(name = "MacManufacturer")
class MacManufacturer {
    @Id
    var mac: String? = null

    @Column(name ="company")
    var companyName: String? = null

    @Transient
    var percent: Float? = null
}