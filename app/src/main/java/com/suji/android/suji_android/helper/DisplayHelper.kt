package com.suji.android.suji_android.helper

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.suji.android.suji_android.basic.BasicApp


class DisplayHelper {
    object Singleton {
        private val displayMetrics = DisplayMetrics()

        private fun getDisplay(): Display {
            return (BasicApp.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }

        fun getDisplaySize(): Point {
            getDisplay().getMetrics(displayMetrics)
            return Point(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }
}