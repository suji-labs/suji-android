package com.suji.android.suji_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.suji.android.suji_android.database.model.Food

@Dao
interface FoodDAO {
    @Query("SELECT * FROM food")
    fun loadAllFood(): LiveData<List<Food>>

    @Insert
    fun insert(food: Food)

    @Delete
    fun deleteFood(food: Food)

    @Update
    fun updateFood(food: Food)
}