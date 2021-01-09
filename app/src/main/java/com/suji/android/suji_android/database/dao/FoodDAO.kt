package com.suji.android.suji_android.database.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.suji.android.suji_android.database.model.Food
import io.reactivex.Flowable

@Dao
interface FoodDAO {
    @Query("SELECT * FROM food")
    fun loadAllFood(): Flowable<MutableList<Food>>

    @Query("SELECT * FROM food")
    fun loadAllFood2(): MutableList<Food>

    @Insert
    fun insert(food: Food)

    @Delete
    fun delete(food: Food)

    @Update
    fun update(food: Food)
}