package com.example.sitstandtimer.data

interface TimerRepository {

    fun startTimerRunningNotification(type: String)
    fun startTimerFinishedNotification(type: String, isSilent: Boolean)
    fun cancelWorker(tag: String)
    fun stopTimerRunningNotification()
    fun stopTimerFinishedNotification()
}