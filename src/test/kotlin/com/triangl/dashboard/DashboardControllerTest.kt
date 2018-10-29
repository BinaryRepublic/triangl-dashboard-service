package com.triangl.dashboard

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.triangl.dashboard.controller.DashboardController
import com.triangl.dashboard.dto.VisitorCountRespDto
import com.triangl.dashboard.dto.VisitorCountTimeframeDto
import com.triangl.dashboard.services.DashboardService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(MockitoJUnitRunner::class)
@WebMvcTest
class DashboardControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var dashboardService: DashboardService

    @InjectMocks
    private lateinit var dashboardController: DashboardController

    @Before
    fun init() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(dashboardController)
            .build()
    }

    @Test
    fun `should return a list of all customers`() {
        /* Given */
        val jsonPayload = "{ " +
            "\"customerId\": \"customer1\"," +
            "\"from\": \"2018-10-10T09:00:00Z\"," +
            "\"to\": \"2018-10-10T14:00:00Z\"," +
            "\"dataPointCount\": \"5\"" +
        "}"

        val visitorCountRespDto = VisitorCountRespDto(
            data = arrayListOf(
                VisitorCountTimeframeDto(
                    from = "2018-10-10T09:00:00Z",
                    to = "2018-10-10T09:00:00Z",
                    count = 10
                )
            ),
            total = 10
        )

        given(dashboardService.visitorsDuringTimeframe(any())).willReturn(visitorCountRespDto)

        /* When, Then */
        mockMvc
            .perform(
                post("/visitors/count")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPayload))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.`is`(10)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].from", Matchers.`is`("2018-10-10T09:00:00Z")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].to", Matchers.`is`("2018-10-10T09:00:00Z")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].count", Matchers.`is`(10)))
    }
}