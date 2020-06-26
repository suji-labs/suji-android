package com.suji.android.suji_android.database.dao

import androidx.room.*
import com.suji.android.suji_android.database.model.Sale
import io.reactivex.Flowable

@Dao
interface SaleDAO {
    @Query("SELECT * FROM sale")
    fun loadAllSale(): Flowable<List<Sale>>

    @Query("SELECT * FROM sale WHERE sale.isSale IS :isSale")
    fun loadProduct(isSale: Boolean): Flowable<List<Sale>>

    @Query("SELECT * FROM sale WHERE salesTime BETWEEN :start and :end AND isSale IS 1")
    fun findSoldDate(start: Long, end: Long): Flowable<List<Sale>>

    @Query("DELETE FROM sale WHERE salesTime IS 1 BETWEEN :start and :end And isSale IS 1")
    fun deleteSoldDate(start: Long, end: Long)

    @Insert
    fun insert(sale: Sale)

    @Delete
    fun delete(sale: Sale)

    @Update
    fun update(sale: Sale)
}