package com.suji.android.suji_android.helper

import com.suji.android.suji_android.BuildConfig

object Log {
    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(tag, msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, msg)
        }
    }
}