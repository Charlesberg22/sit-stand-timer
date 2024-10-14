package com.example.sitstandtimer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sitstandtimer.TimerApplication
import com.example.sitstandtimer.data.TimerRepository
import com.example.sitstandtimer.data.TimerUiState
import com.example.sitstandtimer.data.UserPreferenceRepository
import com.example.sitstandtimer.utils.combine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val timerRepository: TimerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())

    // using a rewritten combine function as the default only allows 5 flows
    // this is the way I found to combine data sources from preferences and the viewmodel
    val uiState: StateFlow<TimerUiState> = combine(
        _uiState.asStateFlow(),
        userPreferenceRepository.numberOfIntervals,
        userPreferenceRepository.intervalLength,
        userPreferenceRepository.breakLength,
        userPreferenceRepository.lunchLength,
        userPreferenceRepository.snoozeLength
    ) { currentState, numberOfIntervals, intervalLength, breakLength, lunchLength, snoozeLength ->
        currentState.copy(
            numberOfIntervals = numberOfIntervals,
            intervalLength = intervalLength,
            breakLength = breakLength,
            lunchLength = lunchLength,
            snoozeLength = snoozeLength
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimerUiState()
    )

    // to get user preferences repository dependency of viewmodel sorted
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TimerApplication)
                val timerRepository = (this[APPLICATION_KEY] as TimerApplication).container.timerRepository
                TimerViewModel(
                    userPreferenceRepository = application.userPreferenceRepository,
                    timerRepository = timerRepository)
            }
        }
    }

    // Update the sitting/standing status
    fun setStandingOrSitting() {
        _uiState.update { currentState ->
            currentState.copy(
                isStanding = !_uiState.value.isStanding
            )
        }
    }

    fun startTimer(
        timerLength: Float = _uiState.value.intervalLength,
        timerType: String = "stand"
        ) {
        timerRepository.startTimer(timerLength.toLong(), timerType)
        _uiState.update { currentState ->
            currentState.copy(
                timeRemaining = (_uiState.value.intervalLength * 60).toInt()
            )
        }
        viewModelScope.launch {
            timerCountdown()
        }
    }

    // to manually decrease time as shown on screen, as i don't know how to access the worker time
    private suspend fun timerCountdown() {
        while (_uiState.value.timeRemaining > 0) {
            delay(1000L)
            _uiState.update { currentState ->
                val newTimeRemaining = _uiState.value.timeRemaining - 1
                var minutes = (newTimeRemaining / 60).toString()
                var seconds = (newTimeRemaining % 60).toString()
                if (minutes.length == 1) minutes = "0$minutes"
                if (seconds.length == 1) seconds = "0$seconds"

                currentState.copy(
                    timeRemaining = newTimeRemaining,
                    minutesRemaining = minutes,
                    secondsRemaining = seconds
                )
            }
        }
    }

    fun cancelTimers() {
        timerRepository.cancelTimers()
        _uiState.update { currentState ->
            currentState.copy(
                timeRemaining = 0
            )
        }
    }

    // save preferences, with defaults so one can be updated at a time
    fun updatePreferences(
        numberOfIntervals: Int = uiState.value.numberOfIntervals,
        intervalLength: Float = uiState.value.intervalLength,
        breakLength: Float = uiState.value.breakLength,
        lunchLength: Float = uiState.value.lunchLength,
        snoozeLength: Float = uiState.value.snoozeLength
    ) {
        viewModelScope.launch {
            userPreferenceRepository.savePreferences(
                numberOfIntervals = numberOfIntervals,
                intervalLength = intervalLength,
                breakLength = breakLength,
                lunchLength = lunchLength,
                snoozeLength = snoozeLength
            )
        }
    }
}