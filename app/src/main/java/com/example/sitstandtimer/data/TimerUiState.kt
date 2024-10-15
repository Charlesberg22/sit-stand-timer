package com.example.sitstandtimer.data

data class TimerUiState (
    // all timer length storages in minutes
    val intervalLength: Float = 30F,
    val isStanding: Boolean = true,
    val numberOfIntervals: Int = 2,
    val breakLength: Float = 15F,
    val lunchLength: Float = 60F,
    val snoozeLength: Float = 15F,
    val intervalsRemaining: Int = numberOfIntervals,
    val onBreak: Boolean = false,
    val hadLunch: Boolean = false,
    val timeRemainingInSeconds: Int = (intervalLength* 60).toInt(),
    val minutesRemaining: String = intervalLength.toInt().toString(),
    val secondsRemaining: String = "00",
    val isTimerRunning: Boolean = false,
    val isTimerFinished: Boolean = true,
    val isTimeToScanNFC: Boolean = false
)