package com.example.sitstandtimer.utils

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

@Suppress("DEPRECATION")
class VibrationHelper(private val applicationContext: Context) {
    private var vibrator: Vibrator? = null

    fun prepare() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            applicationContext.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun start() {
        val pattern = longArrayOf(0, 250, 500, 750, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            vibrator?.vibrate(pattern, 0)
        }
    }

    fun release() {
        vibrator?.cancel()
        vibrator = null
    }
}