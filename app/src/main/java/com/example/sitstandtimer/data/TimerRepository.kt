package com.example.sitstandtimer.data

interface TimerRepository {
    fun startTimer(timerLength: Long, timerType: String)
    fun cancelTimers()
}