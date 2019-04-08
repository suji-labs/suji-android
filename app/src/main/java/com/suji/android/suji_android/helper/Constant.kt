package com.suji.android.suji_android.helper

import com.suji.android.suji_android.listener.ItemClickListener

object Constant {
    object ViewType {
        const val FOOD_VIEW = 0
        const val SALE_VIEW = 1
        const val SOLD_VIEW = 2
    }

    object ListenerHashMap {
        val listenerList: HashMap<String, ItemClickListener> = HashMap<String, ItemClickListener>()
    }
}