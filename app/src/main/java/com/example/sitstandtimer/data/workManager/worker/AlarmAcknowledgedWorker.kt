package com.example.sitstandtimer.data.workManager.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sitstandtimer.utils.TimerNotificationHelper
import kotlin.coroutines.cancellation.CancellationException

class AlarmAcknowledgedWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val timerNotificationHelper: TimerNotificationHelper
) : CoroutineWorker(context, workerParameters) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return try {

            timerNotificationHelper.removeTimerFinishedNotification()

            Result.success()
        } catch (e: CancellationException) {
            Result.failure()
        }
    }
}