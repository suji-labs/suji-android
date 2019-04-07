package com.suji.android.suji_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.suji.android.suji_android.database.model.Sale

@Dao
interface SaleDAO {
    @Query("SELECT * FROM sale")
    fun loadAllSale(): LiveData<List<Sale>>

    @Query("SELECT * FROM sale WHERE sale.sell IS 1")
    fun loadSold(): LiveData<List<Sale>>

    @Query("SELECT * FROM sale WHERE sale.sell IS 0")
    fun loadSale(): LiveData<List<Sale>>

    @Insert
    fun insert(sale: Sale)

    @Delete
    fun delete(sale: Sale)

    @Update
    fun update(sale: Sale)
}