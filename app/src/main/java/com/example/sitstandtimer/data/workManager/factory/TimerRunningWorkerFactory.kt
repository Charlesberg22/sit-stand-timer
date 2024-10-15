package com.example.sitstandtimer.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.sitstandtimer.data.workManager.worker.TimerRunningWorker
import com.example.sitstandtimer.utils.TimerNotificationHelper

class TimerRunningWorkerFactory(
    private val timerNotificationHelper: TimerNotificationHelper
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            TimerRunningWorker::class.java.name ->
                TimerRunningWorker(appContext, workerParameters, timerNotificationHelper)
            else -> null
        }
    }
}
