package com.triangl.dashboard.services

import com.triangl.dashboard.entity.Customer
import com.triangl.dashboard.webservices.googleSQL.GoogleSQLWs
import org.springframework.stereotype.Service

@Service
class CustomerService (
        val googleSQLWs: GoogleSQLWs
) {
    fun getCustomerById(customerId: String): Customer {
        return googleSQLWs.findCustomerById(customerId)
    }
}