package com.suji.android.suji_android.sell

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.database.repository.DataRepository

class SellViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository = (application as BasicApp).getRepository()

    fun getAllSale(): List<Sale> {
        return repository.loadProduct(false)
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
}