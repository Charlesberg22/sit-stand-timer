package com.example.sitstandtimer

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.sitstandtimer.data.UserPreferenceRepository

class TimerApplication: Application() {
    lateinit var userPreferenceRepository: UserPreferenceRepository

    override fun onCreate() {
        super.onCreate()
        userPreferenceRepository = UserPreferenceRepository(dataStore)
    }
}

private const val PREFERENCE_NAME = "preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCE_NAME
)