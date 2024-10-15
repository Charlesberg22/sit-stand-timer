package com.example.sitstandtimer.data

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sitstandtimer.data.workManager.worker.TIMER_FINISHED_TAG
import com.example.sitstandtimer.data.workManager.worker.TIMER_RUNNING_TAG
import com.example.sitstandtimer.data.workManager.worker.TimerFinishedWorker
import com.example.sitstandtimer.data.workManager.worker.TimerRunningWorker

class WorkManagerTimerRepository(context: Context) : TimerRepository {

    val workManager = WorkManager.getInstance(context)


    override fun startTimerRunningNotification() {
        // add work request
        val timerRunningBuilder = OneTimeWorkRequestBuilder<TimerRunningWorker>()
            .addTag(tag = TIMER_RUNNING_TAG)
            .build()

        //start the work
        workManager.enqueueUniqueWork(
            TIMER_RUNNING_TAG,
            ExistingWorkPolicy.REPLACE,
            timerRunningBuilder
        )
    }

    override fun startTimerFinishedNotification() {
        // add work request
        val timerFinishedBuilder = OneTimeWorkRequestBuilder<TimerFinishedWorker>()
            .addTag(tag = TIMER_FINISHED_TAG)
            .build()

        //start the work
        workManager.enqueueUniqueWork(
            TIMER_FINISHED_TAG,
            ExistingWorkPolicy.REPLACE,
            timerFinishedBuilder
        )
    }

    override fun cancelWorker(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }
}