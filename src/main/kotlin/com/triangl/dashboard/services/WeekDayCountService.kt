package com.triangl.dashboard.services

import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class WeekDayCountService {
    fun countWeekdaysInTimeFrame(from: LocalDateTime, to: LocalDateTime): MutableMap<DayOfWeek, Int> {
        var countWeekDays = HashMap<DayOfWeek, Int>()
        var day = from.toLocalDate()
        while (dayIsInTimeFrame(day, from, to)) {
            countWeekDays[day.dayOfWeek] = countWeekDays.getOrDefault(day.dayOfWeek, 0 ) + 1
            day = day.plusDays(1)
        }
        return countWeekDays
    }

    fun dayIsInTimeFrame (day: LocalDate, from: LocalDateTime, to: LocalDateTime): Boolean {
        return !day.isBefore(from.toLocalDate()) && !day.isAfter(to.toLocalDate())
    }
}