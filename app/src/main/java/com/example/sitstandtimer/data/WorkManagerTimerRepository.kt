package com.example.sitstandtimer.data

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow

class WorkManagerTimerRepository(context: Context) : TimerRepository {

    private val workManager = WorkManager.getInstance(context)

    override val outputWorkInfo: Flow<WorkInfo> {
        TODO("Not yet implemented")
    }

    override fun startTimer(timerLength: Float) {
        TODO("Not yet implemented")
    }

    override fun cancelTimers() {
        TODO("Not yet implemented")
    }
}