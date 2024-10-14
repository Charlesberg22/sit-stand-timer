package com.example.sitstandtimer.data

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    val outputWorkInfo: Flow<WorkInfo>
    fun startTimer(timerLength: Float)
    fun cancelTimers()
}