package com.suji.android.suji_android.callback

import com.suji.android.suji_android.database.model.Food

interface FoodClickListener {
    fun onDeleteClick(food: Food)
    fun onModifyClick()
}