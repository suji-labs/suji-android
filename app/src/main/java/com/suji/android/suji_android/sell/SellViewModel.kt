package com.suji.android.suji_android.sell

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.database.repository.DataRepository

class SellViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository
    private var sales: MediatorLiveData<List<Sale>> = MediatorLiveData()

    init {
        sales.value = null
        repository = (application as BasicApp).getRepository()
        sales.addSource(repository.sale, object : Observer<List<Sale>> {
            override fun onChanged(sales: List<Sale>?) {
                this@SellViewModel.sales.value = sales
            }
        })
    }

    fun getAllSale(): LiveData<List<Sale>> {
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