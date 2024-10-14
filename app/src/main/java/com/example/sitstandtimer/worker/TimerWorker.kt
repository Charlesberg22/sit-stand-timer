package com.example.sitstandtimer.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class TimerWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val timerType = inputData.getString(typeKey)

        makeTimerOverNotification(
            when (timerType) {
                "break", "lunch" -> "Time to go back to work"
                "sit" -> "Time to stand up"
                "stand" -> "Time to sit down"
                "work" -> "Time to take a break"
                else -> "Time to stop whatever you're doing"
            },
            applicationContext
        )
        return Result.success()
    }

    companion object {
        const val typeKey = "TYPE"
    }
}