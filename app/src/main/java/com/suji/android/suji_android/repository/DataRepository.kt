package com.suji.android.suji_android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.executor.AppExecutors
import com.suji.android.suji_android.database.model.Food

class DataRepository private constructor(private val database: AppDatabase) {
    private val observableMemo: MediatorLiveData<List<Food>> = MediatorLiveData()
    private val executors: AppExecutors = AppExecutors()

    val food: LiveData<List<Food>>
        get() = observableMemo

    init {
        observableMemo.addSource(this.database.foodDAO().loadAllFood(), object : Observer<List<Food>> {
            override fun onChanged(t: List<Food>?) {
                observableMemo.postValue(t)
            }
        })
    }

    fun addFood(food: Food) {
        executors.diskIO().execute(Runnable { database.foodDAO().insert(food) })
    }

    fun deleteFood(food: Food) {
        executors.diskIO().execute(Runnable { database.foodDAO().deleteFood(food) })
    }

    fun updateFood(food: Food) {
        executors.diskIO().execute(Runnable { database.foodDAO().updateFood(food) })
    }

    object Singleton {
        private lateinit var INSTANCE: DataRepository

        fun getInstance(database: AppDatabase): DataRepository {
            INSTANCE = DataRepository(database)
            return INSTANCE
        }
    }
}