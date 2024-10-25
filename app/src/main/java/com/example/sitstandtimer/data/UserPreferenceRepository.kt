package com.example.sitstandtimer.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        const val TAG = "UserPreferencesRepo"

        // Keys
        private val NUMBER_OF_INTERVALS = intPreferencesKey("number_of_intervals")
        private val INTERVAL_LENGTH = floatPreferencesKey("interval_length")
        private val BREAK_LENGTH = floatPreferencesKey("break_length")
        private val LUNCH_LENGTH = floatPreferencesKey("lunch_length")
        private val SNOOZE_LENGTH = floatPreferencesKey("snooze_length")
        private val REMOTE_NFC_TAG = stringPreferencesKey("remote_nfc_tag")
        private val DESK_NFC_TAG = stringPreferencesKey("desk_nfc_tag")

        // Default Values
        private const val DEFAULT_NUMBER_OF_INTERVALS = 2
        private const val DEFAULT_INTERVAL_LENGTH = 30f
        private const val DEFAULT_BREAK_LENGTH = 15f
        private const val DEFAULT_LUNCH_LENGTH = 60f
        private const val DEFAULT_SNOOZE_LENGTH = 15f
        private const val DEFAULT_NFC_TAG = ""
    }

    private fun <T> Flow<Preferences>.getPreferenceWithFallback(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> = this
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[key] ?: defaultValue
        }

    val numberOfIntervals: Flow<Int> = dataStore.data
        .getPreferenceWithFallback(NUMBER_OF_INTERVALS, DEFAULT_NUMBER_OF_INTERVALS)

    val intervalLength: Flow<Float> = dataStore.data
        .getPreferenceWithFallback(INTERVAL_LENGTH, DEFAULT_INTERVAL_LENGTH)

    val breakLength: Flow<Float> = dataStore.data
        .getPreferenceWithFallback(BREAK_LENGTH, DEFAULT_BREAK_LENGTH)

    val lunchLength: Flow<Float> = dataStore.data
        .getPreferenceWithFallback(LUNCH_LENGTH, DEFAULT_LUNCH_LENGTH)

    val snoozeLength: Flow<Float> = dataStore.data
        .getPreferenceWithFallback(SNOOZE_LENGTH, DEFAULT_SNOOZE_LENGTH)

    val remoteNfcTag: Flow<String> = dataStore.data
        .getPreferenceWithFallback(REMOTE_NFC_TAG, DEFAULT_NFC_TAG)

    val deskNfcTag: Flow<String> = dataStore.data
        .getPreferenceWithFallback(DESK_NFC_TAG, DEFAULT_NFC_TAG)


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

    suspend fun saveRemoteNfcTag(tag: String, location: NfcTagLocation) {
        val key = when (location) {
            NfcTagLocation.DESK -> DESK_NFC_TAG
            NfcTagLocation.REMOTE -> REMOTE_NFC_TAG
        }
        dataStore.edit { preferences ->
            preferences[key] = tag
        }
    }
}

enum class NfcTagLocation {
    DESK, REMOTE
}

