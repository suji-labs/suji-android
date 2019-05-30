package com.suji.android.suji_android.helper

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object Utils {
    val format: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")

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
}