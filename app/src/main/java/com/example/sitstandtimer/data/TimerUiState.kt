package com.example.sitstandtimer.data

data class TimerUiState (
    val timerLength: Float = 30F,
    val isStanding: Boolean = true,
    val intervalsRemaining: Int = 0,
    val hadLunch: Boolean = false,
)