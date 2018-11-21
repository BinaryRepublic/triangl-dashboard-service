package com.triangl.dashboard.repository

import com.triangl.dashboard.entity.Customer
import org.springframework.data.repository.Repository

interface CustomerRepository: Repository<Customer, String> {

    fun findById(customerId: String): Customer

}