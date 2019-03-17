package com.suji.android.suji_android

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Food::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuDAO(): MenuDAO

    object Singleton {
        private const val DATABASE_NAME = "suji-android"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = buildDatabase(context.applicationContext)
//                        insertData(INSTANCE!!)
                    }
                }
            }
            return INSTANCE
        }

        /**
         * 데이터 베이스 생성
         * addCallback가 작동되지 않음
         * 이유는 모르겠음
         */
        private fun buildDatabase(appContext: Context): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
//                        val database = getInstance(appContext)
//                        insertData(database!!)
                        Log.i("AppDataBase", "after insert data")
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
        }

        private fun insertData(database: AppDatabase) {
            Thread(Runnable {
                database.runInTransaction {
                    database.menuDAO().insert(Food("보리밥", 6000))
                    database.menuDAO().insert(Food("뚝배기 묶은지 쪽갈비", 7000))
                    database.menuDAO().insert(Food("닭볶음탕", 20000))
                }
            }).start()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS menu (name TEXT, price LONG)")
            }
        }
    }
}