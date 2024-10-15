package com.example.sitstandtimer.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.sitstandtimer.MainActivity
import com.example.sitstandtimer.R
import com.example.sitstandtimer.utils.GlobalProperties.pendingIntentFlags

class TimerNotificationHelper(
    private val applicationContext : Context
) {
    private val notificationManager = NotificationManagerCompat.from(applicationContext)

    private val openTimerIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.sitstandtimer.com/Running".toUri(),
        applicationContext,
        MainActivity::class.java
    ).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }

    private val openTimerPendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        openTimerIntent,
        pendingIntentFlags
    )

    private val openAlarmIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.sitstandtimer.com/Finished".toUri(),
        applicationContext,
        MainActivity::class.java
    ).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }

    private val openAlarmPendingIntent = PendingIntent.getActivity(
        applicationContext,
        2,
        openAlarmIntent,
        pendingIntentFlags
    )

    init {
        createTimerNotificationChannels()
    }

    fun showTimerRunningNotification() =
        NotificationCompat.Builder(applicationContext, TIMER_RUNNING_CHANNEL)
            .setContentTitle("Time remaining in current position")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(openTimerPendingIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            .build()

    fun removeTimerRunningNotification() {
        notificationManager.cancel(TIMER_RUNNING_NOTIFICATION_ID)
    }

    fun showTimerFinishedNotification() =
        NotificationCompat.Builder(applicationContext, TIMER_FINISHED_CHANNEL)
            .setContentTitle("Time to change position")
            .setFullScreenIntent(openAlarmPendingIntent, true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setOngoing(true)
            .build()

    fun removeTimerFinishedNotification() {
        notificationManager.cancel(TIMER_FINISHED_NOTIFICATION_ID)
    }

    private fun createTimerNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timerRunningChannel = NotificationChannel(
                TIMER_RUNNING_CHANNEL,
                TIMER_RUNNING_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
                timerRunningChannel.name = "Timer Running Channel"
                timerRunningChannel.description = "Shows notification when timer running"
                timerRunningChannel.setSound(null, null)

            val timerFinishedChannel = NotificationChannel(
                TIMER_FINISHED_CHANNEL,
                TIMER_FINISHED_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH
            )
                timerFinishedChannel.name = "Timer Finished Channel"
                timerFinishedChannel.description = "Shows notification when timer finished"
                timerFinishedChannel.setSound(null, null)

            notificationManager.createNotificationChannels(
                listOf(
                    timerRunningChannel,
                    timerFinishedChannel
                )
            )
        }
    }

}

private const val TIMER_RUNNING_CHANNEL = "timer_running_channel"
private const val TIMER_FINISHED_CHANNEL = "timer_completed_channel"
const val TIMER_RUNNING_NOTIFICATION_ID = 3
const val TIMER_FINISHED_NOTIFICATION_ID = 4