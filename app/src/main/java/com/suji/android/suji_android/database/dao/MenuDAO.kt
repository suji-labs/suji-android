package com.suji.android.suji_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.suji.android.suji_android.database.model.Food

@Dao
interface MenuDAO {
    @Query("SELECT * FROM food")
    fun loadAllFood(): LiveData<List<Food>>

    @Insert
    fun insert(food: Food)

    @Delete
    fun deleteFood(food: Food)
}