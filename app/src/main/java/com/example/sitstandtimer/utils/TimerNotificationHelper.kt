package com.example.sitstandtimer.utils

import android.annotation.SuppressLint
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
    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    init {
        createTimerRunningNotificationChannel()
        createTimerFinishedNotificationChannel()
    }

    private fun createTimerRunningNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                TIMER_RUNNING_CHANNEL,
                "Timer running channel",
                importance
            )
            channel.description = "Shows notification when timer running"

            notificationManager?.createNotificationChannel(channel)
        }
    }

    private val openTimerIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.sitstandtimer.com/Timer".toUri(),
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

    private fun timerRunningBuilder(type: String?) =
        NotificationCompat.Builder(applicationContext, TIMER_RUNNING_CHANNEL)
            .setContentTitle(
                when (type) {
                    "STAND" -> "You should be standing"
                    "SIT" -> "You should be sitting"
                    "BREAK" -> "You should be on a break"
                    "LUNCH" -> "You should be having lunch"
                    else -> "Error: I don't know what you should be doing"
                }
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(openTimerPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)

    @SuppressLint("MissingPermission")
    fun showTimerRunningNotification(type: String?) =
        NotificationManagerCompat.from(applicationContext).notify(TIMER_RUNNING_NOTIFICATION_ID, timerRunningBuilder(type).build())

    private fun createTimerFinishedNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                TIMER_FINISHED_CHANNEL,
                "Timer finished channel",
                importance
            )
            channel.description = "Shows notification when timer finished"

            notificationManager?.createNotificationChannel(channel)
        }
    }

    private val openAlarmIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.sitstandtimer.com/Alarm".toUri(),
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

    private fun timerFinishedBuilder(type: String?) =
        NotificationCompat.Builder(applicationContext, TIMER_FINISHED_CHANNEL)
            .setContentTitle(
                when (type) {
                "STAND" -> "Time to stand"
                "SIT" -> "Time to sit"
                "BREAK" -> "Time to take a break"
                else -> "Error: I don't know what it's time for"
            } )
            .setFullScreenIntent(openAlarmPendingIntent, true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setOngoing(true)

    @SuppressLint("MissingPermission")
    fun showTimerFinishedNotification(type: String?) {
        NotificationManagerCompat.from(applicationContext).notify(TIMER_FINISHED_NOTIFICATION_ID, timerFinishedBuilder(type).build())
    }

//    fun showTimerRunningNotification() =
//        NotificationCompat.Builder(applicationContext, TIMER_RUNNING_CHANNEL)
//            .setContentTitle("Time remaining in current position")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentIntent(openTimerPendingIntent)
//            .setAutoCancel(true)
//            .setOngoing(true)
//            .build()

    fun removeTimerRunningNotification() {
        notificationManager?.cancel(TIMER_RUNNING_NOTIFICATION_ID)
    }

//    fun showTimerFinishedNotification() =
//        NotificationCompat.Builder(applicationContext, TIMER_FINISHED_CHANNEL)
//            .setContentTitle("Time to change position")
//            .setFullScreenIntent(openAlarmPendingIntent, true)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setAutoCancel(true)
//            .setOngoing(true)
//            .build()

    fun removeTimerFinishedNotification() {
        notificationManager?.cancel(TIMER_FINISHED_NOTIFICATION_ID)
    }

//    private fun createTimerNotificationChannels() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val timerRunningChannel = NotificationChannelCompat.Builder(
//                TIMER_RUNNING_CHANNEL,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//                .setName("Timer Running Channel")
//                .setDescription("Shows notification when timer running")
//                .setSound(null, null)
//                .build()
//
//            val timerFinishedChannel = NotificationChannelCompat.Builder(
//                TIMER_FINISHED_CHANNEL,
//                NotificationManager.IMPORTANCE_MAX
//            )
//                .setName("Timer Finished Channel")
//                .setDescription("Shows notification when timer finished")
//                .setSound(null, null)
//                .build()
//
//            notificationManager.createNotificationChannelsCompat(
//                listOf(
//                    timerRunningChannel,
//                    timerFinishedChannel
//                )
//            )
//        }
//    }

}

private const val TIMER_RUNNING_CHANNEL = "timer_running_channel"
private const val TIMER_FINISHED_CHANNEL = "timer_completed_channel"
const val TIMER_RUNNING_NOTIFICATION_ID = 3
const val TIMER_FINISHED_NOTIFICATION_ID = 4