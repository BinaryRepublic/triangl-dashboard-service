package com.triangl.dashboard

import com.nhaarman.mockito_kotlin.given
import com.triangl.dashboard.entity.Customer
import com.triangl.dashboard.entity.Map
import com.triangl.dashboard.services.CustomerService
import com.triangl.dashboard.webservices.googleSQL.GoogleSQLWs
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CustomerServiceTest {

    @Mock
    private lateinit var googleSQLWs: GoogleSQLWs

    @InjectMocks
    private lateinit var customerService: CustomerService

    @Test
    fun `should return customer by id`() {
        /* Given */
        val customer = Customer().apply {
            id = "TestId"
            name = "TestName"
            maps = setOf(
                Map().apply {
                    name = "TestMap"
                }
            )
        }


        given(googleSQLWs.findCustomerById(customer.id!!)).willReturn(customer)

        /* When */
        val testCustomer = customerService.getCustomerById(customerId = customer.id!!)

        /* Then */
        assertThat(testCustomer.id).isEqualTo(customer.id)
        assertThat(testCustomer.name).isEqualTo(customer.name)
        assertThat(testCustomer.maps!!.first().name).isEqualTo(customer.maps!!.first().name)
    }
}