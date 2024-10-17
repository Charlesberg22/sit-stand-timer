package com.example.sitstandtimer.data

interface TimerRepository {

    fun startTimerRunningNotification(type: String)
    fun startTimerFinishedNotification(type: String)
    fun cancelWorker(tag: String)
    fun stopTimerRunningNotification()
    fun stopTimerFinishedNotification()
}