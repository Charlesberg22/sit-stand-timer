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
    val currentTime: String = "",
    val intervalsRemaining: Int = numberOfIntervals,
    val hadLunch: Boolean = false,
    val timeRemainingInSeconds: Int = (intervalLength * 60).toInt(),
    val minutesRemaining: String = intervalLength.toInt().toString(),
    val secondsRemaining: String = "00",
    val timeToBreak: String = (intervalLength * numberOfIntervals).toInt().toString(),
    val isTimerRunning: Boolean = false, // TODO: assess whether required
    val isTimerFinished: Boolean = true, // TODO: assess whether required
    val isTimeToScanNFC: Boolean = false,
    val timerType: TimerType = TimerType.STAND
)

enum class TimerType(title: String) {
    SIT("sit"),
    STAND("stand"),
    BREAK("break"),
    LUNCH("lunch"),
    //SNOOZE("snooze") // TODO: I think I need to manage this with a boolean only
}