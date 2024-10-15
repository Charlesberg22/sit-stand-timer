package com.example.sitstandtimer.data

interface TimerRepository {

    fun startTimerRunningNotification(type: String)
    fun startTimerFinishedNotification()
    fun cancelWorker(tag: String)
}