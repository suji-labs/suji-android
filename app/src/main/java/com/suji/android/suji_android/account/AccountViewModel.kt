package com.suji.android.suji_android.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import io.reactivex.Flowable
import org.joda.time.DateTime

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    fun getAllSold(): Flowable<List<Sale>> {
        return BasicApp.instance.getRepository().loadProduct(true)
    }

    fun insert(sale: Sale) {
        BasicApp.instance.getRepository().insert(sale)
    }

    fun delete(sale: Sale) {
        BasicApp.instance.getRepository().delete(sale)
    }

    fun update(sale: Sale) {
        BasicApp.instance.getRepository().update(sale)
    }

    fun findSaleOfDate(start: DateTime, end: DateTime): Flowable<List<Sale>> {
        return BasicApp.instance.getRepository().findSaleOfDate(start, end)
    }

    fun deleteSoldDate(start: DateTime, end: DateTime) {
        BasicApp.instance.getRepository().deleteSoldDate(start, end)
    }
}