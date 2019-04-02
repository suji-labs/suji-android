package com.suji.android.suji_android.listener

import com.suji.android.suji_android.database.model.Sale

interface FoodSellClickListener {
    fun sell(sale: Sale)
    fun addFood()
    fun cancel(sale: Sale)
}