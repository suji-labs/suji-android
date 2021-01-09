package com.suji.android.suji_android.helper

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAdjusters

object Utils {
    val format: DateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")
    private val dateTime = LocalDateTime.now()

    fun getStartDate(minusMonths: Long): Long {
        return dateTime
            .minusMonths(minusMonths)
            .withDayOfMonth(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getEndDate(minusMonths: Long): Long {
        return dateTime
            .minusMonths(minusMonths)
            .with(TemporalAdjusters.lastDayOfMonth())
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getStartWeek(): Long {
        return dateTime
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getEndWeek(): Long {
        return dateTime
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getStartTime(): Long {
        return dateTime
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getEndTime(): Long {
        return dateTime
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}