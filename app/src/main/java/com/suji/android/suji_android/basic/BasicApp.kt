package com.suji.android.suji_android.basic

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.suji.android.suji_android.database.AppDatabase
import com.suji.android.suji_android.database.repository.Repository

class BasicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        instance = this
    }

    fun getDatabase(): AppDatabase {
        return AppDatabase.Singleton.getInstance()
    }

    fun getRepository(): Repository {
        return Repository
    }

    companion object {
        lateinit var instance: BasicApp
            private set
    }

    fun getContext(): Context = applicationContext
}