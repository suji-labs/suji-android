package com.suji.android.suji_android.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.basic.BasicApp
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.database.repository.DataRepository

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: DataRepository
    private var sales: MediatorLiveData<List<Sale>> = MediatorLiveData()

    init {
        sales.value = null
        repository = (application as BasicApp).getRepository()
        val sold = repository.sold
        sales.addSource(sold, object : Observer<List<Sale>> {
            override fun onChanged(sales: List<Sale>?) {
                this@AccountViewModel.sales.value = sales
            }
        })
    }

    fun getAllSold(): LiveData<List<Sale>> {
        return sales
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