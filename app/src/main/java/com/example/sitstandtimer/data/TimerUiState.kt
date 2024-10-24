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
    val remoteNfcTag: String = "",
    val deskNfcTag: String = "",
    val isNfcOn: Boolean = false,
    val isSilentModeOn: Boolean = false,


    // variables that aren't settings
    val currentTime: String = "",
    val intervalsRemaining: Int = numberOfIntervals,
    val hadLunch: Boolean = false,
    val timeRemainingInSeconds: Int = (intervalLength * 60).toInt(),
    val minutesRemaining: String = intervalLength.toInt().toString(),
    val secondsRemaining: String = "00",
    val timeToBreak: String = (intervalLength * numberOfIntervals).toInt().toString(),
    val isTimeToScanNFC: Boolean = false,
    val timerType: TimerType = TimerType.STAND,
    val onSnooze: Boolean = false,
)

enum class TimerType {
    SIT,
    STAND,
    BREAK,
    LUNCH
}