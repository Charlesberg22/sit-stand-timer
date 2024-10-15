package com.example.sitstandtimer.data.workManager.worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.sitstandtimer.ui.TimerViewModel
import com.example.sitstandtimer.utils.MediaPlayerHelper
import com.example.sitstandtimer.utils.TIMER_FINISHED_NOTIFICATION_ID
import com.example.sitstandtimer.utils.TimerNotificationHelper
import kotlinx.coroutines.flow.collectLatest
import kotlin.coroutines.cancellation.CancellationException

class TimerFinishedWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val viewModel: TimerViewModel,
    private val timerNotificationHelper: TimerNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            mediaPlayerHelper.prepare()
            mediaPlayerHelper.start()

            val foregroundInfo = ForegroundInfo(
                TIMER_FINISHED_NOTIFICATION_ID,
                timerNotificationHelper.showTimerFinishedNotification(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0
            )

            setForeground(foregroundInfo)

            viewModel.uiState.collectLatest {}

            Result.success()
        } catch (e: CancellationException) {
            mediaPlayerHelper.release()
            timerNotificationHelper.removeTimerFinishedNotification()
            Result.failure()
        }
    }
}

const val TIMER_FINISHED_TAG = "timerFinishedTag"