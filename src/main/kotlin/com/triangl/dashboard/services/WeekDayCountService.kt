package com.triangl.dashboard.services

import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class WeekDayCountService {
    fun occurrencesOfWeekDaysInTimeframe(from: LocalDateTime, to: LocalDateTime): MutableMap<DayOfWeek, Int> {
        val occurrencePerWeekDay = HashMap<DayOfWeek, Int>()
        var dayIterator = from.toLocalDate()
        while (dayIsInTimeFrame(dayIterator, from, to)) {
            occurrencePerWeekDay[dayIterator.dayOfWeek] = occurrencePerWeekDay.getOrDefault(dayIterator.dayOfWeek, 0 ) + 1
            dayIterator = dayIterator.plusDays(1)
        }
        return occurrencePerWeekDay
    }

    fun dayIsInTimeFrame (day: LocalDate, from: LocalDateTime, to: LocalDateTime): Boolean {
        return !day.isBefore(from.toLocalDate()) && !day.isAfter(to.toLocalDate())
    }
}