package com.suji.android.suji_android.helper

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.suji.android.suji_android.basic.BasicApp

object DisplayHelper {
    private fun getDisplay(): Display {
        return (BasicApp.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    }

    fun getDisplaySize(): Point {
        DisplayMetrics().let {
            getDisplay().getMetrics(it)
            return Point(it.widthPixels, it.heightPixels)
        }
    }
}