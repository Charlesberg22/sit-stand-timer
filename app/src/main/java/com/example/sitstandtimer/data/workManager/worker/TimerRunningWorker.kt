package com.example.sitstandtimer.data.workManager.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.sitstandtimer.utils.TIMER_RUNNING_NOTIFICATION_ID
import com.example.sitstandtimer.utils.TimerNotificationHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

class TimerRunningWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val timerNotificationHelper: TimerNotificationHelper
) : CoroutineWorker(context, workerParameters) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return try {

            val type = inputData.getString(typeKey)

            val notification = timerNotificationHelper.timerRunningBuilder(type)

            setForeground(ForegroundInfo(TIMER_RUNNING_NOTIFICATION_ID, notification))

            delay(60 * 60 * 1000)

            Result.success()
        } catch (e: CancellationException) {
            timerNotificationHelper.removeTimerRunningNotification()
            Result.failure()
        }
    }

    companion object {
        const val typeKey = "TYPE"
    }
}

const val TIMER_RUNNING_TAG = "timerRunningTag"