package com.suji.android.suji_android.listener

interface DialogClickListener {
//    fun createFood()
//    fun addSubMenuClick()
//    fun cancelFood()

    interface OnClickListener {
        fun onClick()
    }
    fun positive()
    fun negative()
    fun neutral()
}