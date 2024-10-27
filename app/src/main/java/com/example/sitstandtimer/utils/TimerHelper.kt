package com.example.sitstandtimer.utils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

// Credit: https://github.com/yassineAbou/Clock

abstract class TimerHelper(
    private val durationInSeconds: Int
) {
    private var job: Job? = null
    private var remainingTime: Int = 0
    private var isTimerPaused: Boolean = true

    val seconds = MutableStateFlow("00")
    val minutes = MutableStateFlow("00")

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
                    minutes.value = (remainingTime / 60).toString()
                    seconds.value = (remainingTime % 60).toString()
                    if (minutes.value.length == 1) minutes.value = "0${minutes.value}"
                    if (seconds.value.length == 1) seconds.value = "0${seconds.value}"
                }
                onTimerFinish()
                reset()
            }
            isTimerPaused = false
        }
    }

    fun reset() {
        job?.cancel()
        remainingTime = durationInSeconds
        isTimerPaused = true
    }

    abstract fun onTimerTick(remainingTime: Int)
    abstract fun onTimerFinish()
}