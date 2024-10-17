package com.example.sitstandtimer.data.workManager.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sitstandtimer.utils.MediaPlayerHelper
import com.example.sitstandtimer.utils.TimerNotificationHelper
import kotlin.coroutines.cancellation.CancellationException

class TimerFinishedWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val timerNotificationHelper: TimerNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            //mediaPlayerHelper.prepare()
            //mediaPlayerHelper.start()

            val timerType = inputData.getString(typeKey)

            timerNotificationHelper.showTimerFinishedNotification(timerType)

            Result.success()
        } catch (e: CancellationException) {
            //mediaPlayerHelper.release()
            timerNotificationHelper.removeTimerFinishedNotification()
            Result.failure()
        }
    }

    companion object {
        const val typeKey = "TYPE"
    }
}

const val TIMER_FINISHED_TAG = "timerFinishedTag"