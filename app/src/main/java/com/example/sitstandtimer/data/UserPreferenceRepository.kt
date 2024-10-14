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
        val BREAK_LENGTH = floatPreferencesKey("break_length")
        val LUNCH_LENGTH = floatPreferencesKey("lunch_length")
        val SNOOZE_LENGTH = floatPreferencesKey("snooze_length")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun savePreferences(
        numberOfIntervals: Int,
        intervalLength: Float,
        breakLength: Float,
        lunchLength: Float,
        snoozeLength: Float
    ) {
        dataStore.edit { preferences ->
            preferences[NUMBER_OF_INTERVALS] = numberOfIntervals
            preferences[INTERVAL_LENGTH] = intervalLength
            preferences[BREAK_LENGTH] = breakLength
            preferences[LUNCH_LENGTH] = lunchLength
            preferences[SNOOZE_LENGTH] = snoozeLength
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

    val breakLength: Flow<Float> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[BREAK_LENGTH] ?: 15f
        }

    val lunchLength: Flow<Float> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LUNCH_LENGTH] ?: 60f
        }

    val snoozeLength: Flow<Float> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SNOOZE_LENGTH] ?: 15f
        }
}