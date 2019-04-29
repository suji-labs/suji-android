package com.suji.android.suji_android.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.database.repository.DataRepository
import org.joda.time.DateTime

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository
    private var solds: MediatorLiveData<List<Sale>> = MediatorLiveData()

    init {
        solds.value = null
        repository = (application as BasicApp).getRepository()
        solds.addSource(repository.sold, object : Observer<List<Sale>> {
            override fun onChanged(sales: List<Sale>?) {
                this@AccountViewModel.solds.value = sales
            }
        })
    }

    fun getAllSold(): LiveData<List<Sale>> {
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

    fun deleteSoleDate(start: DateTime, end: DateTime) {
        repository.deleteSoldDate(start, end)
    }
}