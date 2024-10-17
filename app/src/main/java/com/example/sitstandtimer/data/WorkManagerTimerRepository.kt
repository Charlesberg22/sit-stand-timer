package com.example.sitstandtimer.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sitstandtimer.data.workManager.worker.AlarmAcknowledgedWorker
import com.example.sitstandtimer.data.workManager.worker.TIMER_FINISHED_TAG
import com.example.sitstandtimer.data.workManager.worker.TIMER_RUNNING_TAG
import com.example.sitstandtimer.data.workManager.worker.TimerCancelledWorker
import com.example.sitstandtimer.data.workManager.worker.TimerFinishedWorker
import com.example.sitstandtimer.data.workManager.worker.TimerRunningWorker

class WorkManagerTimerRepository(context: Context) : TimerRepository {

    private val workManager = WorkManager.getInstance(context)


    override fun startTimerRunningNotification(type: String) {
        val data = Data.Builder()
        data.putString(TimerRunningWorker.typeKey, type)

        // add work request
        val timerRunningBuilder = OneTimeWorkRequestBuilder<TimerRunningWorker>()
            .addTag(tag = TIMER_RUNNING_TAG)
            .setInputData(data.build())
            .build()

        //start the work
        workManager.enqueueUniqueWork(
            type,
            ExistingWorkPolicy.REPLACE,
            timerRunningBuilder
        )
    }

    override fun startTimerFinishedNotification(type: String) {
        val data = Data.Builder()
        data.putString(TimerRunningWorker.typeKey, type)


        // add work request
        val timerFinishedBuilder = OneTimeWorkRequestBuilder<TimerFinishedWorker>()
            .addTag(tag = TIMER_FINISHED_TAG)
            .setInputData(data.build())
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

    override fun stopTimerRunningNotification() {
        // add work request
        val timerCancelledBuilder = OneTimeWorkRequestBuilder<TimerCancelledWorker>()
            .build()

        //start the work
        workManager.enqueueUniqueWork(
            "cancel",
            ExistingWorkPolicy.REPLACE,
            timerCancelledBuilder
        )
    }

    override fun stopTimerFinishedNotification() {
        // add work request
        val alarmAcknowledgedBuilder = OneTimeWorkRequestBuilder<AlarmAcknowledgedWorker>()
            .build()

        //start the work
        workManager.enqueueUniqueWork(
            "acknowledge",
            ExistingWorkPolicy.REPLACE,
            alarmAcknowledgedBuilder
        )
    }
}