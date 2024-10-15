package com.example.sitstandtimer.data

interface TimerRepository {

    fun startTimerRunningNotification()
    fun startTimerFinishedNotification()
    fun cancelWorker(tag: String)
}