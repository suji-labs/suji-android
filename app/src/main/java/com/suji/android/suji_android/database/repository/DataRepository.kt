package com.suji.android.suji_android.database.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.executor.AppExecutors
import org.joda.time.DateTime
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataRepository private constructor(private val database: AppDatabase) {
    private val observableFood: MediatorLiveData<List<Food>> = MediatorLiveData()
    private val observableSale: MediatorLiveData<List<Sale>> = MediatorLiveData()
    private val observableSold: MediatorLiveData<List<Sale>> = MediatorLiveData()
    private val executors: AppExecutors = AppExecutors()

    val food: LiveData<List<Food>> get() = observableFood
    val sale: LiveData<List<Sale>> get() = observableSale
    val sold: LiveData<List<Sale>> get() = observableSold

    init {
        observableFood.addSource(this.database.foodDAO().loadAllFood(), object : Observer<List<Food>> {
            override fun onChanged(t: List<Food>) {
                observableFood.postValue(t)
            }
        })

        observableSale.addSource(this.database.saleDAO().loadProduct(false), object : Observer<List<Sale>> {
            override fun onChanged(t: List<Sale>) {
                observableSale.postValue(t)
            }
        })

        observableSold.addSource(this.database.saleDAO().loadProduct(true), object : Observer<List<Sale>> {
            override fun onChanged(t: List<Sale>) {
                observableSold.postValue(t)
            }
        })
    }

    fun insert(o: Any) {
        when (o) {
            is Food -> executors.diskIO().execute(Runnable { database.foodDAO().insert(o) })
            is Sale -> executors.diskIO().execute(Runnable { database.saleDAO().insert(o) })
        }
    }

    fun delete(o: Any) {
        when (o) {
            is Food -> executors.diskIO().execute(Runnable { database.foodDAO().delete(o) })
            is Sale -> executors.diskIO().execute(Runnable { database.saleDAO().delete(o) })
        }
    }

    fun update(o: Any) {
        when (o) {
            is Food -> executors.diskIO().execute(Runnable { database.foodDAO().update(o) })
            is Sale -> executors.diskIO().execute(Runnable { database.saleDAO().update(o) })
        }
    }

    fun loadProduct(isSale: Boolean): LiveData<List<Sale>> {
        return executors.diskIO().submit(Callable { database.saleDAO().loadProduct(isSale) }).get()
    }

    fun findSaleOfDate(start: DateTime, end: DateTime): List<Sale> {
        return executors.diskIO().submit(Callable<List<Sale>> { database.saleDAO().findSaleOfDate(start, end) }).get()
    }

    object Singleton {
        private lateinit var INSTANCE: DataRepository

        fun getInstance(database: AppDatabase): DataRepository {
            INSTANCE = DataRepository(database)
            return INSTANCE
        }
    }
}