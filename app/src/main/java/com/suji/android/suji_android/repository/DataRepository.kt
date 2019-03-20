package com.suji.android.suji_android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.executor.AppExecutors
import com.suji.android.suji_android.model.Food

class DataRepository private constructor(private val database: AppDatabase) {
    private val observableMemo: MediatorLiveData<List<Food>> = MediatorLiveData()
    private val executors: AppExecutors = AppExecutors()

    val menu: LiveData<List<Food>>
        get() = observableMemo

    init {
        observableMemo.addSource(this.database.menuDAO().loadAllFood(), object : Observer<List<Food>> {
            override fun onChanged(t: List<Food>?) {
                observableMemo.postValue(t)
            }
        })
    }

    fun addMemo(menu: Food) {
        executors.diskIO().execute(Runnable { database.menuDAO().insert(menu) })
    }

    fun deleteMemo(menu: Food) {
        executors.diskIO().execute(Runnable { database.menuDAO().deleteFood(menu) })
    }

    object Singleton {
        private lateinit var INSTANCE: DataRepository

        fun getInstance(database: AppDatabase): DataRepository {
            INSTANCE = DataRepository(database)
            return INSTANCE
        }
    }
}