package com.example.sitstandtimer.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sitstandtimer.worker.TimerWorker
import java.util.concurrent.TimeUnit

class WorkManagerTimerRepository(context: Context) : TimerRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun startTimer(timerLength: Long, timerType: String) {
        // set up input data
        val data = Data.Builder()
        data.putString(TimerWorker.typeKey, timerType)

        // add WorkRequest
        val timerBuilder = OneTimeWorkRequestBuilder<TimerWorker>()
            .setInitialDelay(timerLength, TimeUnit.MINUTES)
            .setInputData(data.build())
            .build()

        // start the work
        workManager.enqueueUniqueWork(
            timerType,
            ExistingWorkPolicy.REPLACE,
            timerBuilder
        )
    }

    override fun cancelTimers() {
        workManager.cancelAllWork()
    }
}