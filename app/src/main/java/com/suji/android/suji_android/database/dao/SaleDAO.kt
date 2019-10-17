package com.suji.android.suji_android.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.suji.android.suji_android.database.model.Sale
import org.joda.time.DateTime

@Dao
interface SaleDAO {
    @Query("SELECT * FROM sale")
    fun loadAllSale(): LiveData<List<Sale>>

    @Query("SELECT * FROM sale WHERE sale.isSale IS :isSale")
    fun loadProduct(isSale: Boolean): LiveData<List<Sale>>

    @Query("SELECT * FROM sale WHERE time BETWEEN :start and :end")
    fun findSaleOfDate(start: DateTime, end: DateTime): List<Sale>

    @Query("DELETE FROM sale WHERE sale.isSale IS 1 BETWEEN :start and :end")
    fun deleteSoldDate(start: DateTime, end: DateTime)

    @Insert
    fun insert(sale: Sale)

    @Delete
    fun delete(sale: Sale)

    @Update
    fun update(sale: Sale)
}