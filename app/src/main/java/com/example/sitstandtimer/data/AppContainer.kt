package com.example.sitstandtimer.data

import android.content.Context

interface AppContainer {
    val timerRepository: WorkManagerTimerRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    override val timerRepository = WorkManagerTimerRepository(context)
}