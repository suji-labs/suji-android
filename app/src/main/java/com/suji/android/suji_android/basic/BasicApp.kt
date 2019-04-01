package com.suji.android.suji_android.basic

import android.app.Application
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.database.repository.DataRepository

class BasicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        BasicApp.app = applicationContext as BasicApp
    }

    private fun getDatabase(): AppDatabase {
        return AppDatabase.Singleton.getInstance(this)!!
    }

    fun getRepository(): DataRepository {
        return DataRepository.Singleton.getInstance(getDatabase())
    }

    companion object {
        lateinit var app: BasicApp
    }
}