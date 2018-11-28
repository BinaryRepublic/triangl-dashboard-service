package com.triangl.dashboard.repository.utilsDB

import com.triangl.dashboard.dbModels.utilsDB.entity.MacManufacturer
import org.springframework.data.repository.Repository

interface ManufacturerRepository: Repository<MacManufacturer, String> {
    fun findByMacIn(macs: List<String>): List<MacManufacturer>
}