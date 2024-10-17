package com.example.sitstandtimer.data

data class TimerUiState (
    // User settings
    // all timer length storages in minutes
    val intervalLength: Float = 30F,
    val isStanding: Boolean = true,
    val numberOfIntervals: Int = 2,
    val breakLength: Float = 15F,
    val lunchLength: Float = 60F,
    val snoozeLength: Float = 15F,

    // variables that aren't settings
    val intervalsRemaining: Int = numberOfIntervals,
    val hadLunch: Boolean = false,
    val timeRemainingInSeconds: Int = (intervalLength * 60).toInt(),
    val minutesRemaining: String = intervalLength.toInt().toString(),
    val secondsRemaining: String = "00",
    val isTimerRunning: Boolean = false,
    val isTimerFinished: Boolean = true,
    val isTimeToScanNFC: Boolean = false,
    val timerType: TimerType = TimerType.STAND
)

enum class TimerType {
    SIT,
    STAND,
    BREAK,
    LUNCH,
}