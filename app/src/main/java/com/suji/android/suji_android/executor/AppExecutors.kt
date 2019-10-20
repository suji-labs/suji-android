package com.suji.android.suji_android.executor

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Executors.newFixedThreadPool

object AppExecutors {
    private val diskIO: ExecutorService = Executors.newSingleThreadExecutor()
    private val networkIO: ExecutorService = newFixedThreadPool(3)
    private val mainThread: Executor = MainThreadExecutor()

    fun diskIO(): ExecutorService {
        return diskIO
    }

    fun networkIO(): ExecutorService {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}