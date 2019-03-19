package com.suji.android.suji_android.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager



class DisplayHelper {
    @SuppressLint("StaticFieldLeak")
    object Singleton {
        private lateinit var context: Context
        private val displayMetrics = DisplayMetrics()

        fun setContext(context: Context) {
            Singleton.context = context
        }

        private fun getDisplay(): Display {
            return (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }

        fun getDisplaySize(): Point {
            getDisplay()
                .getMetrics(displayMetrics)
            return Point(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
    }
}