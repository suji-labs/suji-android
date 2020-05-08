package com.suji.android.suji_android.database.dao

import androidx.room.*
import com.suji.android.suji_android.database.model.Sale
import io.reactivex.Flowable
import org.joda.time.DateTime

@Dao
interface SaleDAO {
    @Query("SELECT * FROM sale")
    fun loadAllSale(): Flowable<List<Sale>>

    @Query("SELECT * FROM sale WHERE sale.isSale IS :isSale")
    fun loadProduct(isSale: Boolean): Flowable<List<Sale>>

    @Query("SELECT * FROM sale WHERE time BETWEEN :start and :end")
    fun findSaleOfDate(start: DateTime, end: DateTime): Flowable<List<Sale>>

    @Query("DELETE FROM sale WHERE sale.isSale IS 1 BETWEEN :start and :end")
    fun deleteSoldDate(start: DateTime, end: DateTime)

    @Insert
    fun insert(sale: Sale)

    @Delete
    fun delete(sale: Sale)

    @Update
    fun update(sale: Sale)
}