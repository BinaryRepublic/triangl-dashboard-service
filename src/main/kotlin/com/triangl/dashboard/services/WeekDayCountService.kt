package com.triangl.dashboard.services

import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class WeekDayCountService {
    fun occurrencesOfWeekDaysInTimeframe(from: LocalDate, to: LocalDate): MutableMap<DayOfWeek, Int> {
        val occurrencePerWeekDay = HashMap<DayOfWeek, Int>()
        var dayIterator = from
        while (dayIsInTimeFrame(dayIterator, from, to)) {
            occurrencePerWeekDay[dayIterator.dayOfWeek] = occurrencePerWeekDay.getOrDefault(dayIterator.dayOfWeek, 0 ) + 1
            dayIterator = dayIterator.plusDays(1)
        }
        return occurrencePerWeekDay
    }

    fun dayIsInTimeFrame (day: LocalDate, from: LocalDate, to: LocalDate): Boolean {
        return !day.isBefore(from) && !day.isAfter(to)
    }
}