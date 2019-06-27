package com.suji.android.suji_android.helper

import android.app.AlertDialog
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object Utils {
    val format: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")
    private val days = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val dateTime = DateTime()

    fun nullCheck(item: Any?): Boolean {
        if (item == null) {
            return true
        }
        return false
    }

    fun blankString(item: String): Boolean {
        if (item == "") {
            return true
        }
        return false
    }

    fun dialogReSizing(dialog: AlertDialog) {
        dialog.window!!.attributes = dialog.window!!.attributes.apply {
            DisplayHelper.getDisplaySize().let { point ->
                width = (point.x * 0.9).toInt()
//                height = (point.y * 0.5).toInt()
            }
        }
    }

    private fun getDay(minusMonths: Int): Int {
        return if (dateTime.monthOfYear == 2) {
            if (dateTime.withYear(dateTime.year).year().isLeap) {
                29
            } else {
                28
            }
        } else {
            days[dateTime.minusMonths(minusMonths).monthOfYear - 1]
        }
    }

    fun getStartDate(minusMonths: Int): DateTime {
        return dateTime
            .withMonthOfYear(dateTime.minusMonths(minusMonths).monthOfYear)
            .withDayOfMonth(1)
            .withHourOfDay(0)
            .withMinuteOfHour(0)
            .withSecondOfMinute(0)
    }

    fun getEndDate(minusMonths: Int): DateTime {
        return dateTime
            .withMonthOfYear(dateTime.minusMonths(minusMonths).monthOfYear)
            .withDayOfMonth(getDay(minusMonths))
            .withHourOfDay(23)
            .withMinuteOfHour(59)
            .withSecondOfMinute(59)
    }

    fun getStartWeek(): DateTime {
        return dateTime
            .withDayOfWeek(DateTimeConstants.MONDAY)
            .withHourOfDay(0)
            .withMinuteOfHour(0)
            .withSecondOfMinute(0)
    }

    fun getEndWeek(): DateTime {
        return dateTime
            .withDayOfWeek(DateTimeConstants.SUNDAY)
            .withHourOfDay(23)
            .withMinuteOfHour(59)
            .withSecondOfMinute(59)
    }

    fun getStartTime(): DateTime {
        return dateTime
            .withHourOfDay(0)
            .withMinuteOfHour(0)
            .withSecondOfMinute(0)
    }

    fun getEndTime(): DateTime {
        return dateTime
            .withHourOfDay(23)
            .withMinuteOfHour(59)
            .withSecondOfMinute(59)
    }
}