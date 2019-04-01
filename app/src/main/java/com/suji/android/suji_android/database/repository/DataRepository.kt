package com.suji.android.suji_android.database.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.database.model.Sale
import com.suji.android.suji_android.executor.AppExecutors

class DataRepository private constructor(private val database: AppDatabase) {
    private val observableFood: MediatorLiveData<List<Food>> = MediatorLiveData()
    private val observableSale: MediatorLiveData<List<Sale>> = MediatorLiveData()
    private val executors: AppExecutors = AppExecutors()

    val food: LiveData<List<Food>> get() = observableFood

    val sale: LiveData<List<Sale>> get() = observableSale

    init {
        observableFood.addSource(this.database.foodDAO().loadAllFood(), object : Observer<List<Food>> {
            override fun onChanged(t: List<Food>) {
                observableFood.postValue(t)
            }
        })

        observableSale.addSource(this.database.saleDAO().loadAllSale(), object : Observer<List<Sale>> {
            override fun onChanged(t: List<Sale>) {
                observableSale.postValue(t)
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

//    fun insertFood(food: Food) {
//        executors.diskIO().execute(Runnable { database.foodDAO().insert(food) })
//    }
//
//    fun deleteFood(food: Food) {
//        executors.diskIO().execute(Runnable { database.foodDAO().delete(food) })
//    }
//
//    fun updateFood(food: Food) {
//        executors.diskIO().execute(Runnable { database.foodDAO().update(food) })
//    }
//
//    fun insertSale(sale: Sale) {
//        executors.diskIO().execute(Runnable { database.saleDAO().insert(sale) })
//    }
//
//    fun deleteSale(sale: Sale) {
//        executors.diskIO().execute(Runnable { database.saleDAO().delete(sale) })
//    }
//
//    fun updateSale(sale: Sale) {
//        executors.diskIO().execute(Runnable { database.saleDAO().update(sale) })
//    }

    object Singleton {
        private lateinit var INSTANCE: DataRepository

        fun getInstance(database: AppDatabase): DataRepository {
            INSTANCE = DataRepository(database)
            return INSTANCE
        }
    }
}