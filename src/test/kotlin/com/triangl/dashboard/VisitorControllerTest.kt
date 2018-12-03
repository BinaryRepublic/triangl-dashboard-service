package com.triangl.dashboard

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.triangl.dashboard.controller.VisitorController
import com.triangl.dashboard.dto.*
import com.triangl.dashboard.services.VisitorService
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
import java.time.DayOfWeek

@RunWith(MockitoJUnitRunner::class)
@WebMvcTest
class VisitorControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var dashboardService: VisitorService

    @InjectMocks
    private lateinit var dashboardController: VisitorController

    @Before
    fun init() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(dashboardController)
            .build()
    }

    @Test
    fun `should return a list of timeframe's with their visitorCount`() {
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

    @Test
    fun `should return visitor's average dwell time per given are`() {
        /* Given */
        val jsonPayload = "{ " +
            "\"mapId\": \"map1\"," +
            "\"from\": \"2018-10-10T09:00:00Z\"," +
            "\"to\": \"2018-10-10T14:00:00Z\"," +
            "\"areaDtos\": [" +
                "{ " +
                    "\"corner1\": { \"x\": \"0\", \"y\": \"0\" }," +
                    "\"corner2\": { \"x\": \"100\", \"y\": \"100\" }" +
                "},{" +
                    "\"corner1\": { \"x\": \"101\", \"y\": \"101\" }," +
                    "\"corner2\": { \"x\": \"300\", \"y\": \"300\" }" +
                "}" +
            "]" +
        "}"

        println(jsonPayload)

        val areaDwellTimeResult = listOf(
            AreaDto(
                corner1 = LocationDto(
                    x= 0F,
                    y= 0F
                ),
                corner2 = LocationDto(
                    x= 100F,
                    y= 100F
                ),
                dwellTime = 60
            ),
            AreaDto(
                corner1 = LocationDto(
                        x= 101F,
                        y= 101F
                ),
                corner2 = LocationDto(
                        x= 300F,
                        y= 300F
                ),
                dwellTime = 120
            )
        )

        given(dashboardService.getVisitorsDurationByArea(any())).willReturn(areaDwellTimeResult)

        /* When, Then */
        mockMvc
            .perform(
                post("/visitors/areas/duration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPayload))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].corner1.x", Matchers.`is`(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].corner1.y", Matchers.`is`(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].corner2.x", Matchers.`is`(100.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].corner2.y", Matchers.`is`(100.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dwellTime", Matchers.`is`(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].corner1.x", Matchers.`is`(101.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].corner1.y", Matchers.`is`(101.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].corner2.x", Matchers.`is`(300.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].corner2.y", Matchers.`is`(300.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dwellTime", Matchers.`is`(120)))
    }

    @Test
    fun `should return average visitor count per weekday per hour`() {
        /* Given */
        val jsonPayload = "{ " +
            "\"customerId\": \"customer1\"," +
            "\"from\": \"2018-10-10T09:00:00Z\"," +
            "\"to\": \"2018-10-10T14:00:00Z\"" +
        "}"

        val dwellTimePerWeekDayPerHour = arrayListOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ).map {
            VisitorByTimeAverageRespDto(
                day = it.name.toLowerCase().capitalize()
            ).apply {
                values.add(
                    VisitorAverageTimeframeDto(
                        from = "2000-01-1T00:00:00Z",
                        to = "2000-01-1T00:00:00Z",
                        average = 2.0
                    )
                )
            }
        }

        given(dashboardService.getVisitorCountByTimeOfDayAverage(any())).willReturn(ArrayList(dwellTimePerWeekDayPerHour))

        /* When, Then */
        mockMvc
            .perform(
                post("/visitors/byTimeOfDay/average")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPayload))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<VisitorAverageTimeframeDto>(5)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].values", Matchers.hasSize<VisitorAverageTimeframeDto>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].values", Matchers.hasSize<VisitorAverageTimeframeDto>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].values", Matchers.hasSize<VisitorAverageTimeframeDto>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[3].values", Matchers.hasSize<VisitorAverageTimeframeDto>(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[4].values", Matchers.hasSize<VisitorAverageTimeframeDto>(1)))

    }

    @Test
    fun `should return percentage per manufacturer`() {
        /* Given */
        val jsonPayload = "{ " +
            "\"customerId\": \"customer1\"," +
            "\"from\": \"2018-10-10T09:00:00Z\"," +
            "\"to\": \"2018-10-10T14:00:00Z\"" +
        "}"

        val macManufacturer1 = ManufacturerDto(name = "Apple").apply { percent = 0.50f }
        val macManufacturer2 = ManufacturerDto(name = "Samsung").apply { percent = 0.30f }
        val macManufacturer3 = ManufacturerDto(name = "One Plus").apply { percent = 0.20f }

        given(dashboardService.getPercentageOfManufactures(any())).willReturn(listOf(macManufacturer1,macManufacturer2,macManufacturer3))
        /* When, Then */
        mockMvc
            .perform(
                post("/visitors/manufacturers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonPayload))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<VisitorAverageTimeframeDto>(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.`is`("Apple")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].percent",  Matchers.`is`(0.50)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name",  Matchers.`is`("Samsung")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].percent",  Matchers.`is`(0.30)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].name",  Matchers.`is`("One Plus")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].percent",  Matchers.`is`(0.20)))
    }
}