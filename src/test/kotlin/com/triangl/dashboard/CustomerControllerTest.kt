package com.triangl.dashboard

import com.nhaarman.mockito_kotlin.given
import com.triangl.dashboard.controller.CustomerController
import com.triangl.dashboard.dbModels.servingDB.entity.Customer
import com.triangl.dashboard.dbModels.servingDB.entity.Map
import com.triangl.dashboard.services.CustomerService
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(MockitoJUnitRunner::class)
@WebMvcTest
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var customerService: CustomerService

    @InjectMocks
    private lateinit var customerController: CustomerController

    @Before
    fun init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .build()
    }

    @Test
    fun `should requested customer by id`() {
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

        given(customerService.getCustomerById(customer.id!!)).willReturn(customer)

        /* When, Then */
        mockMvc
            .perform(
                get("/customers/${customer.id}")
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.`is`(customer.id)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.`is`(customer.name)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.maps[0].name", Matchers.`is`(customer.maps!!.first().name)))
    }
}