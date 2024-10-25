package com.example.sitstandtimer.data.workManager.worker

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
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

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val type = inputData.getString(typeKey)
        val notification = timerNotificationHelper.timerRunningBuilder(type)
        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0
        return ForegroundInfo(TIMER_RUNNING_NOTIFICATION_ID, notification, foregroundServiceType)
    }

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return try {
            setForeground(getForegroundInfo())

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