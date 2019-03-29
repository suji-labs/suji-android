package com.suji.android.suji_android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.database.model.Food
import com.suji.android.suji_android.executor.AppExecutors

class DataRepository private constructor(private val database: AppDatabase) {
    private val observableFood: MediatorLiveData<List<Food>> = MediatorLiveData()
    private val executors: AppExecutors = AppExecutors()

    val food: LiveData<List<Food>>
        get() = observableFood

    init {
        observableFood.addSource(this.database.foodDAO().loadAllFood(), object : Observer<List<Food>> {
            override fun onChanged(t: List<Food>) {
                observableFood.postValue(t)
            }
        })
    }

    fun insert(food: Food) {
        executors.diskIO().execute(Runnable { database.foodDAO().insert(food) })
    }

    fun delete(food: Food) {
        executors.diskIO().execute(Runnable { database.foodDAO().delete(food) })
    }

    fun update(food: Food) {
        executors.diskIO().execute(Runnable { database.foodDAO().update(food) })
    }

    object Singleton {
        private lateinit var INSTANCE: DataRepository

        fun getInstance(database: AppDatabase): DataRepository {
            INSTANCE = DataRepository(database)
            return INSTANCE
        }
    }
}