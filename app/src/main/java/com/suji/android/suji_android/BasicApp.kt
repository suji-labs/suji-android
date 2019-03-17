package com.suji.android.suji_android

import android.app.Application

class BasicApp : Application() {

    private fun getDatabase(): AppDatabase {
        return AppDatabase.Singleton.getInstance(this)!!
    }

    fun getRepository(): DataRepository {
        return DataRepository.Singleton.getInstance(getDatabase())
    }
}