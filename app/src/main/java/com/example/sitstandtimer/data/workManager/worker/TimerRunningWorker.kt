package com.example.sitstandtimer.data.workManager.worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.sitstandtimer.ui.TimerViewModel
import com.example.sitstandtimer.utils.TIMER_RUNNING_NOTIFICATION_ID
import com.example.sitstandtimer.utils.TimerNotificationHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest

class TimerRunningWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val viewModel: TimerViewModel,
    private val timerNotificationHelper: TimerNotificationHelper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            val foregroundInfo = ForegroundInfo(
                TIMER_RUNNING_NOTIFICATION_ID,
                timerNotificationHelper.getTimerBaseNotification().build(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0
            )

            setForeground(foregroundInfo)

            viewModel.uiState.collectLatest {
                if (!it.isTimerFinished) {
                    timerNotificationHelper.updateTimerServiceNotification(
                        minutesRemaining = it.minutesRemaining,
                        secondsRemaining = it.secondsRemaining
                    )
                }
            }

            Result.success()
        } catch (e: CancellationException) {
            timerNotificationHelper.removeTimerRunningNotification()
            Result.failure()
        }
    }
}

const val TIMER_RUNNING_TAG = "timerRunningTag"