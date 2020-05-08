package com.suji.android.suji_android.database.dao

import androidx.room.*
import com.suji.android.suji_android.database.model.Food
import io.reactivex.Flowable

@Dao
interface FoodDAO {
    @Query("SELECT * FROM food")
    fun loadAllFood(): Flowable<List<Food>>

    @Insert
    fun insert(food: Food)

    @Delete
    fun delete(food: Food)

    @Update
    fun update(food: Food)
}