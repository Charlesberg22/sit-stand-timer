package com.example.sitstandtimer.data

data class TimerUiState (
    // all timer length storages in minutes
    val timerLength: Float = 30F,
    val isStanding: Boolean = true,
    val numberOfIntervals: Int = 2,
    val intervalsRemaining: Int = 2,
    val hadLunch: Boolean = false,
)