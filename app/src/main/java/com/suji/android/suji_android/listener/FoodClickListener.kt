package com.suji.android.suji_android.listener

import com.suji.android.suji_android.database.model.Food

interface FoodClickListener {
    fun onDeleteClick(food: Food)
    fun onModifyClick(food: Food)
}