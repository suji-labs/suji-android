package com.suji.android.suji_android.helper

object Helper {
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