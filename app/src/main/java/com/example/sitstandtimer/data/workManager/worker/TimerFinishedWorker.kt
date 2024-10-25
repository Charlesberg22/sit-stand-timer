package com.example.sitstandtimer.data.workManager.worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.sitstandtimer.utils.MediaPlayerHelper
import com.example.sitstandtimer.utils.TIMER_FINISHED_NOTIFICATION_ID
import com.example.sitstandtimer.utils.TimerNotificationHelper
import com.example.sitstandtimer.utils.VibrationHelper
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

class TimerFinishedWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val timerNotificationHelper: TimerNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val vibrationHelper: VibrationHelper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val type = inputData.getString(typeKey)
        val notification = timerNotificationHelper.timerFinishedBuilder(type)
        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0
        return ForegroundInfo(TIMER_FINISHED_NOTIFICATION_ID, notification, foregroundServiceType)
    }

    override suspend fun doWork(): Result {
        return try {

            if (inputData.getBoolean(silentKey, false)) {
                vibrationHelper.prepare()
                vibrationHelper.start()
            } else {
                mediaPlayerHelper.prepare()
                mediaPlayerHelper.start()
            }

            setForeground(getForegroundInfo())
            timerNotificationHelper.wakeScreenWhenTimerFinished()

            // only show the notification for 60 seconds
            delay(1000 * 60 )
            vibrationHelper.release()
            mediaPlayerHelper.release()

            Result.success()
        } catch (e: CancellationException) {
            mediaPlayerHelper.release()
            vibrationHelper.release()
            timerNotificationHelper.removeTimerFinishedNotification()
            Result.failure()
        }
    }

    companion object {
        const val typeKey = "TYPE"
        const val silentKey = "SILENT"
    }
}

const val TIMER_FINISHED_TAG = "timerFinishedTag"