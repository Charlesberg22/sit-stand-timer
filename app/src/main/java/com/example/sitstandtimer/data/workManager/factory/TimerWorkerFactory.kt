package com.example.sitstandtimer.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.sitstandtimer.data.workManager.worker.AlarmAcknowledgedWorker
import com.example.sitstandtimer.data.workManager.worker.TimerCancelledWorker
import com.example.sitstandtimer.data.workManager.worker.TimerFinishedWorker
import com.example.sitstandtimer.data.workManager.worker.TimerRunningWorker
import com.example.sitstandtimer.utils.MediaPlayerHelper
import com.example.sitstandtimer.utils.TimerNotificationHelper
import com.example.sitstandtimer.utils.VibrationHelper

class TimerWorkerFactory(
    private val timerNotificationHelper: TimerNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val vibrationHelper: VibrationHelper
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            TimerRunningWorker::class.java.name ->
                TimerRunningWorker(appContext, workerParameters, timerNotificationHelper)
            TimerFinishedWorker::class.java.name ->
                TimerFinishedWorker(appContext, workerParameters, timerNotificationHelper, mediaPlayerHelper, vibrationHelper)
            TimerCancelledWorker::class.java.name ->
                TimerCancelledWorker(appContext, workerParameters, timerNotificationHelper)
            AlarmAcknowledgedWorker::class.java.name ->
                AlarmAcknowledgedWorker(appContext, workerParameters, timerNotificationHelper, mediaPlayerHelper, vibrationHelper)
            else -> null
        }
    }
}
