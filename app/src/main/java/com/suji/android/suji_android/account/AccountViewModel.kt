package com.suji.android.suji_android.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.database.repository.DataRepository
import org.joda.time.DateTime

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository = (application as BasicApp).getRepository()

    fun getAllSold(): List<Sale> {
        return repository.loadProduct(true)
    }

    fun insert(sale: Sale) {
        repository.insert(sale)
    }

    fun delete(sale: Sale) {
        repository.delete(sale)
    }

    fun update(sale: Sale) {
        repository.update(sale)
    }

    fun findSaleOfDate(start: DateTime, end: DateTime): List<Sale> {
        return repository.findSaleOfDate(start, end)
    }
}