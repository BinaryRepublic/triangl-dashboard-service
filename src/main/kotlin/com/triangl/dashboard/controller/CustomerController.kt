package com.triangl.dashboard.controller

import com.triangl.dashboard.dbModels.servingDB.entity.Customer
import com.triangl.dashboard.services.CustomerService
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@CrossOrigin
class CustomerController (
        val customerService: CustomerService
) {
    @ApiOperation(value = "Get customer by ID", response = Customer::class)
    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: String): ResponseEntity<*> {
        val customer = customerService.getCustomerById(id)

        return ResponseEntity.ok().body(customer)
    }
}