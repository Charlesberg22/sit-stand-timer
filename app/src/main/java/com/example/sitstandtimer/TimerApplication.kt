package com.example.sitstandtimer

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.Configuration
import com.example.sitstandtimer.data.AppContainer
import com.example.sitstandtimer.data.DefaultAppContainer
import com.example.sitstandtimer.data.UserPreferenceRepository
import com.example.sitstandtimer.data.workManager.factory.TimerWorkerFactory
import com.example.sitstandtimer.utils.MediaPlayerHelper
import com.example.sitstandtimer.utils.TimerNotificationHelper

class TimerApplication: Application(), Configuration.Provider {
    lateinit var userPreferenceRepository: UserPreferenceRepository
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        userPreferenceRepository = UserPreferenceRepository(dataStore)
        container = DefaultAppContainer(this)
    }

    override val workManagerConfiguration: Configuration
        get() {
        val timerNotificationHelper = TimerNotificationHelper(this)
        val mediaPlayerHelper = MediaPlayerHelper(this)

        return Configuration.Builder()
            .setWorkerFactory(TimerWorkerFactory(timerNotificationHelper, mediaPlayerHelper))
            .build()

    }
}

private const val PREFERENCE_NAME = "preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCE_NAME
)