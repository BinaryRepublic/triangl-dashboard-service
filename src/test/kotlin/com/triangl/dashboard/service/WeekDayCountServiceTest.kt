package com.triangl.dashboard.service

import com.triangl.dashboard.services.WeekDayCountService
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import java.time.DayOfWeek
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class WeekDayCountServiceTest {

    @InjectMocks
    private lateinit var weekDayCountService: WeekDayCountService

    @Test
    fun `should return true for daysInTimeFrame`() {
        /* Given */
        val start = LocalDate.of(2018,3,29)
        val end = LocalDate.of(2018,6,20)
        val daysWhichAreInTimeFrame = arrayListOf(
                LocalDate.of(2018,5,1),
                LocalDate.of(2018,6,20),
                LocalDate.of(2018,3,29),
                LocalDate.of(2018,5,23)
        )
        /* When, Then */
        for (day in daysWhichAreInTimeFrame) {
            assertThat(weekDayCountService.dayIsInTimeFrame(day, start, end), `is`(true))
        }
    }

    @Test
    fun `should return false for daysNotInTimeFrame`() {
        /* Given */
        val start = LocalDate.of(2018,3,29)
        val end = LocalDate.of(2018,6,20)
        val daysWhichAreInTimeFrame = arrayListOf(
                LocalDate.of(2018,1,1),
                LocalDate.of(2018,6,21),
                LocalDate.of(2018,3,28),
                LocalDate.of(2018,11,23)
        )
        /* When, Then */
        for (day in daysWhichAreInTimeFrame) {
            assertThat(weekDayCountService.dayIsInTimeFrame(day, start, end), `is`(false))
        }
    }

    @Test
    fun `should return right occurrence counts for every weekDay`() {
        /* Given */
        val start = LocalDate.of(2018,4,22)
        val end = LocalDate.of(2018,6,3)
        val occurrencePerWeekDay = hashMapOf(
            DayOfWeek.MONDAY to 6,
            DayOfWeek.TUESDAY to 6,
            DayOfWeek.WEDNESDAY to 6,
            DayOfWeek.THURSDAY to 6,
            DayOfWeek.FRIDAY to 6,
            DayOfWeek.SATURDAY to 6,
            DayOfWeek.SUNDAY to 7
        )

        /* When */
        val testResult = weekDayCountService.occurrencesOfWeekDaysInTimeframe(start, end)

        /* Then */
        for ((dayOfWeek, occurrenceCount) in occurrencePerWeekDay) {
            assertThat(occurrenceCount, `is`(testResult[dayOfWeek]))
        }

    }
}