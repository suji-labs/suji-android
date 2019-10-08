package com.suji.android.suji_android.helper

import com.suji.android.suji_android.listener.ItemClickListener

object Constant {
    object PayType {
        const val CARD = 0
        const val CASH = 1
    }

    object ListenerHashMap {
        val listenerList: HashMap<String, ItemClickListener> = HashMap<String, ItemClickListener>()
    }
}