package com.example.sitstandtimer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sitstandtimer.TimerApplication
import com.example.sitstandtimer.data.TimerUiState
import com.example.sitstandtimer.data.UserPreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TimerViewModel(
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
//    val uiStatePlain: StateFlow<TimerUiState> = _uiState.asStateFlow()
//
//    val uiStateNumberOfIntervals: StateFlow<TimerUiState> =
//        userPreferenceRepository.numberOfIntervals.map { numberOfIntervals ->
//            TimerUiState(numberOfIntervals = numberOfIntervals)
//        }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = TimerUiState()
//            )

    val uiState: StateFlow<TimerUiState> = combine(
        _uiState.asStateFlow(),
        userPreferenceRepository.numberOfIntervals
    ) { currentState, numberOfIntervals ->
        currentState.copy(
            numberOfIntervals = numberOfIntervals
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimerUiState()
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TimerApplication)
                TimerViewModel(application.userPreferenceRepository)
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
}