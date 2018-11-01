package com.triangl.dashboard

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.triangl.dashboard.dto.*
import com.triangl.dashboard.entity.Coordinate
import com.triangl.dashboard.entity.TrackingPoint
import com.triangl.dashboard.helper.InstantHelper
import com.triangl.dashboard.projection.TrackingPointCoordinateJoin
import com.triangl.dashboard.services.DashboardService
import com.triangl.dashboard.services.WeekDayCountService
import com.triangl.dashboard.webservices.googleSQL.GoogleSQLWs
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


@RunWith(MockitoJUnitRunner::class)
class DashboardServiceTest {

    @Mock
    private lateinit var googleSQLWs: GoogleSQLWs

    @Mock
    private lateinit var weekDayCountService: WeekDayCountService

    @InjectMocks
    private lateinit var dashboardService: DashboardService

    private val instantHelper = InstantHelper()

    private val from = Instant.parse("2018-10-10T09:00:00Z")

    private val to = from.plus(9, ChronoUnit.DAYS)

    private val timestampsToCreate = listOf(
        from,
        from.plusSeconds(20),
        from.plusSeconds(40),
        from,
        from.plusSeconds(30)
    )
    private val coordinatesToCreate = listOf(
        Coordinate().apply { id = "1"; x = 10f; y = 20f },
        Coordinate().apply { id = "2"; x = 10f; y = 20f },
        Coordinate().apply { id = "3"; x = 20f; y = 30f },
        Coordinate().apply { id = "4"; x = 10f; y = 20f },
        Coordinate().apply { id = "5"; x = 10f; y = 20f }
    )
    private val trackedDeviceIdsToCreate = listOf("1", "1", "2", "2", "2")

    @Test
    fun `should return visitorsDuringTimeFrame`() {
        /* Given */
        val dataPointCount = 3
        val visitorCountReqDto = VisitorCountReqDto(
                customerId = "CustomerId",
                from = from,
                to = from.plus(
                    dataPointCount.toLong(),
                    ChronoUnit.HOURS),
                dataPointCount = dataPointCount
        )

        given(googleSQLWs.countDistinctDeviceIdsInTimeFrame(
                anyString(),
                any(),
                any()
        )).willReturn(2)

        /* When */
        val result = dashboardService.visitorsDuringTimeframe(visitorCountReqDto)

        /* Then */
        val expectedResult = VisitorCountRespDto(
            data = arrayListOf(
                VisitorCountTimeframeDto(
                    from = "2018-10-10T09:00:00Z",
                    to = "2018-10-10T09:59:59.999999999Z",
                    count = 2
                ),
                VisitorCountTimeframeDto(
                    from = "2018-10-10T10:00:00Z",
                    to = "2018-10-10T10:59:59.999999999Z",
                    count = 2
                ),
                VisitorCountTimeframeDto(
                    from = "2018-10-10T11:00:00Z",
                    to = "2018-10-10T11:59:59.999999999Z",
                    count = 2
                )
            ),
            total = 2
        )

        assertThat(result.total)        .isEqualTo(expectedResult.total)
        assertThat(result.data[0].to)   .isEqualTo(expectedResult.data[0].to)
        assertThat(result.data[0].from) .isEqualTo(expectedResult.data[0].from)
        assertThat(result.data[0].count).isEqualTo(expectedResult.data[0].count)
        assertThat(result.data[1].to)   .isEqualTo(expectedResult.data[1].to)
        assertThat(result.data[1].from) .isEqualTo(expectedResult.data[1].from)
        assertThat(result.data[1].count).isEqualTo(expectedResult.data[1].count)
        assertThat(result.data[2].to)   .isEqualTo(expectedResult.data[2].to)
        assertThat(result.data[2].from) .isEqualTo(expectedResult.data[2].from)
        assertThat(result.data[2].count).isEqualTo(expectedResult.data[2].count)
    }

    @Test
    fun `should return dwellTime per area`() {
        /* Given */
        val dbTrackingPointList = arrayListOf<TrackingPointCoordinateJoin>()
        for (index in 0..trackedDeviceIdsToCreate.lastIndex) {
            dbTrackingPointList.add(TrackingPointCoordinateJoin().apply {
                coordinate = coordinatesToCreate[index]
                trackedDeviceId = trackedDeviceIdsToCreate[index]
                timestamp = timestampsToCreate[index]
            })
        }

        val visitorAreaDurationReqDto = VisitorAreaDurationReqDto(
            mapId = "1",
            from = from,
            to = from.plusSeconds(40),
            areaDtos = listOf(
                AreaDto(
                    corner1 = LocationDto(x = 0F, y = 0F),
                    corner2 = LocationDto(x = 20F, y = 20F)
                ),
                AreaDto(
                    corner1 = LocationDto(x = 21F, y = 21F),
                    corner2 = LocationDto(x = 40F, y = 40F)
                )
            )
        )

        given(googleSQLWs.selectAllDeviceIdWithCoordinateInTimeframe(anyString(),any(),any())).willReturn(dbTrackingPointList)

        /* When */
        val result = dashboardService.getVisitorsDurationByArea(visitorAreaDurationReqDto)

        /* Then */
        assertThat(result[0].dwellTime).isEqualTo(25)
        assertThat(result[1].dwellTime).isEqualTo(0)
    }

    @Test
    fun `should return average visitor count per time of day per weekday`() {
        /* Given */
        val dbTrackingPointList = arrayListOf<TrackingPoint>()
        for (index in 0..trackedDeviceIdsToCreate.lastIndex) {
            dbTrackingPointList.add(TrackingPoint().apply {
                coordinateId = coordinatesToCreate[index].id
                trackedDeviceId = trackedDeviceIdsToCreate[index]
                timestamp = LocalDateTime.ofInstant(timestampsToCreate[index], ZoneId.of("Europe/Berlin"))
            })
        }

        val visitorByTimeAverageReqDto = VisitorByTimeAverageReqDto(
            customerId = "customer1",
            from = from,
            to = to
        )

        val occurrencesOfWeekDaysInTimeframe = hashMapOf(
                DayOfWeek.MONDAY to 1,
                DayOfWeek.TUESDAY to 1,
                DayOfWeek.WEDNESDAY to 2,
                DayOfWeek.THURSDAY to 2,
                DayOfWeek.FRIDAY to 2,
                DayOfWeek.SATURDAY to 1,
                DayOfWeek.SUNDAY to 1
        )

        given(weekDayCountService.occurrencesOfWeekDaysInTimeframe(any(), any())).willReturn(occurrencesOfWeekDaysInTimeframe)
        given(googleSQLWs.selectAllDeviceIdInTimeframe(
            anyString(),
            any(),
            any())
        ).willReturn(dbTrackingPointList)
        /* When */
        val result = dashboardService.getVisitorCountByTimeOfDayAverage(visitorByTimeAverageReqDto)

        /* Then */
        assertThat(result.size).isEqualTo(5)
        val dayWithData = result.find {
            it.day == instantHelper
                    .toLocalDateTime(from)
                    .dayOfWeek
                    .toString()
                    .toLowerCase()
                    .capitalize()
        }
        assertThat(result)
        assertThat(
            dayWithData!!.values.filter { it.average > 0 }.size
        ).isEqualTo(1)
    }
}