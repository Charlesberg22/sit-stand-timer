package com.example.sitstandtimer.data.workManager.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sitstandtimer.utils.MediaPlayerHelper
import com.example.sitstandtimer.utils.TimerNotificationHelper
import com.example.sitstandtimer.utils.VibrationHelper
import kotlin.coroutines.cancellation.CancellationException

class AlarmAcknowledgedWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val timerNotificationHelper: TimerNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val vibrationHelper: VibrationHelper
) : CoroutineWorker(context, workerParameters) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return try {

            timerNotificationHelper.removeTimerFinishedNotification()
            mediaPlayerHelper.release()
            vibrationHelper.release()


            Result.success()
        } catch (e: CancellationException) {
            Result.failure()
        }
    }
}