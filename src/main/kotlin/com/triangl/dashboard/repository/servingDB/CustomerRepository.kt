package com.triangl.dashboard.repository.servingDB

import com.triangl.dashboard.dbModels.servingDB.entity.Customer
import org.springframework.data.repository.Repository

interface CustomerRepository: Repository<Customer, String> {

    fun findById(customerId: String): Customer?

    fun findByUserId(userId: String): Customer?
}