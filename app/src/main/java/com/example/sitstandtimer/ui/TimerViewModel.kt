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
import com.example.sitstandtimer.data.workManager.worker.TIMER_RUNNING_TAG
import com.example.sitstandtimer.utils.TimerHelper
import com.example.sitstandtimer.utils.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val timerRepository: TimerRepository,
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

    private var timerHelper: TimerHelper? = null

    fun setTimer(duration: Int) {
        timerHelper = object : TimerHelper(durationInSeconds = duration) {
            override fun onTimerTick(remainingTime: Int) {
                _uiState.update { currentState ->
                    var minutes = (remainingTime / 60).toString()
                    var seconds = (remainingTime % 60).toString()
                    if (minutes.length == 1) minutes = "0$minutes"
                    if (seconds.length == 1) seconds = "0$seconds"

                    currentState.copy(
                        timeRemainingInSeconds = remainingTime,
                        minutesRemaining = minutes,
                        secondsRemaining = seconds,
                        isTimerRunning = true
                    )
                }
            }

            override fun onTimerFinish() {
                timerRepository.startTimerFinishedNotification()
                resetTimer()
            }
        }
    }

    fun startTimer() {
        timerHelper?.start()
        _uiState.update { currentState ->
            currentState.copy(isTimerRunning = true)
        }
        if (_uiState.value.isTimerFinished) {
            timerRepository.startTimerRunningNotification()
            _uiState.update { currentState ->
                currentState.copy(isTimerFinished = false)
            }
        }
    }

    fun pauseTimer() {
        timerHelper?.pause()
        _uiState.update { currentState ->
            currentState.copy(isTimerRunning = false)
        }
    }

    fun resetTimer() {
        timerHelper?.reset()
        _uiState.update { currentState ->
            currentState.copy(
                isTimerRunning = false,
                isTimerFinished = true,
                minutesRemaining = _uiState.value.intervalLength.toInt().toString(),
                secondsRemaining = "00"
            )
        }
        timerRepository.cancelWorker(TIMER_RUNNING_TAG)
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