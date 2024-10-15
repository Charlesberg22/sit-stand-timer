package com.example.sitstandtimer.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer

import android.provider.Settings

class MediaPlayerHelper(private val applicationContext: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun prepare() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            isLooping = true
            setDataSource(applicationContext, Settings.System.DEFAULT_ALARM_ALERT_URI)
            prepare()
        }
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

}