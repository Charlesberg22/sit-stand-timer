package com.example.sitstandtimer

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.sitstandtimer.data.AppContainer
import com.example.sitstandtimer.data.DefaultAppContainer
import com.example.sitstandtimer.data.UserPreferenceRepository

class TimerApplication: Application() {
    lateinit var userPreferenceRepository: UserPreferenceRepository
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        userPreferenceRepository = UserPreferenceRepository(dataStore)
        container = DefaultAppContainer(this)
    }
}

private const val PREFERENCE_NAME = "preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCE_NAME
)