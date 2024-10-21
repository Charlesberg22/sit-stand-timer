package com.example.sitstandtimer.utils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class TimerHelper(
    private val durationInSeconds: Int
) {
    private var job: Job? = null
    private var remainingTime: Int = 0
    private var isTimerPaused: Boolean = true

    init {
        remainingTime = durationInSeconds
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun start() {
        if (isTimerPaused) {
            job = GlobalScope.launch {
                while (remainingTime > 0) {
                    delay(1000)
                    remainingTime--
                    onTimerTick(remainingTime)
                }
                onTimerFinish()
                reset()
            }
            isTimerPaused = false
        }
    }

    fun pause() {
        job?.cancel()
        isTimerPaused = true
    }

    fun reset() {
        job?.cancel()
        remainingTime = durationInSeconds
        isTimerPaused = true
    }

    abstract fun onTimerTick(remainingTime: Int)
    abstract fun onTimerFinish()
}