package com.example.sitstandtimer.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val NUMBER_OF_INTERVALS = intPreferencesKey("number_of_intervals")
        val INTERVAL_LENGTH = floatPreferencesKey("interval_length")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun savePreferences(
        numberOfIntervals: Int,
        intervalLength: Float,
    ) {
        dataStore.edit { preferences ->
            preferences[NUMBER_OF_INTERVALS] = numberOfIntervals
            preferences[INTERVAL_LENGTH] = intervalLength
        }
    }

    val numberOfIntervals: Flow<Int> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[NUMBER_OF_INTERVALS] ?: 2
        }

    // not yet used
    val intervalLength: Flow<Float> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[INTERVAL_LENGTH] ?: 30f
        }
}