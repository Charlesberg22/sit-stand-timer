package com.example.sitstandtimer.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sitstandtimer.TimerApplication
import com.example.sitstandtimer.data.NfcTagLocation
import com.example.sitstandtimer.data.TimerRepository
import com.example.sitstandtimer.data.TimerType
import com.example.sitstandtimer.data.TimerUiState
import com.example.sitstandtimer.data.UserPreferenceRepository
import com.example.sitstandtimer.data.workManager.worker.TIMER_FINISHED_TAG
import com.example.sitstandtimer.data.workManager.worker.TIMER_RUNNING_TAG
import com.example.sitstandtimer.utils.TimerHelper
import com.example.sitstandtimer.utils.combine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
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
        userPreferenceRepository.snoozeLength,
        userPreferenceRepository.remoteNfcTag,
        userPreferenceRepository.deskNfcTag
    ) { currentState, numberOfIntervals, intervalLength, breakLength, lunchLength, snoozeLength, remoteNfcTag, deskNfcTag ->
        currentState.copy(
            numberOfIntervals = numberOfIntervals,
            intervalLength = intervalLength,
            breakLength = breakLength,
            lunchLength = lunchLength,
            snoozeLength = snoozeLength,
            remoteNfcTag = remoteNfcTag,
            deskNfcTag = deskNfcTag
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimerUiState()
    )

    // navigation state flow
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo

    // trigger navigation to a specific page
    fun navigateToPage(page: String) {
        _navigateTo.value = page
    }

    // reset navigation after handling
    fun resetNavigation() {
        _navigateTo.value = null
    }

    fun saveNfcTag(tag: String, location: NfcTagLocation) {
        viewModelScope.launch {
            userPreferenceRepository.saveRemoteNfcTag(tag, location)
        }

        _uiState.update { currentState ->
            when (location) {
                NfcTagLocation.DESK -> currentState.copy(deskNfcTag = tag)
                NfcTagLocation.REMOTE -> currentState.copy(remoteNfcTag = tag)
            }
        }
    }

    fun isTagStored(tag: String, location: NfcTagLocation): Boolean {
        return if (location == NfcTagLocation.DESK) {
            uiState.value.deskNfcTag == tag
        } else {
            uiState.value.remoteNfcTag == tag
        }
    }

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
                isStanding = !_uiState.value.isStanding,
                timerType = if (_uiState.value.isStanding) TimerType.SIT else TimerType.STAND
            )
        }
    }

    fun switchNfcOnAndOff() {
        if (uiState.value.remoteNfcTag != "" && uiState.value.deskNfcTag != "") {
            _uiState.update { currentState ->
                currentState.copy(
                    isNfcOn = !_uiState.value.isNfcOn
                )
            }
        }
    }

    fun setSilentOrNoisy() {
        _uiState.update { currentState ->
            currentState.copy(
                isSilentModeOn = !_uiState.value.isSilentModeOn
            )
        }
    }

    fun swapTimerType() {
        timerRepository.stopTimerRunningNotification()
        timerRepository.cancelWorker(TIMER_RUNNING_TAG)
        if (_uiState.value.timerType == TimerType.SIT || _uiState.value.timerType == TimerType.STAND) {
            setStandingOrSitting()
            timerRepository.startTimerRunningNotification(_uiState.value.timerType.name)
        } else {
            endTimer()
            _uiState.update { currentState ->
                currentState.copy(
                    timerType = if (_uiState.value.isStanding) TimerType.STAND else TimerType.SIT,
                    onSnooze = false
                )
            }
            setTimer()
            startTimer()
        }

    }

    fun startManualBreak() {
        endTimer()
        _uiState.update { currentState ->
            currentState.copy(
                timerType = TimerType.BREAK
            )
        }
        setTimer()
        startTimer()
    }

    fun startLunch() {
        endTimer()
        _uiState.update { currentState ->
            currentState.copy(
                timerType = TimerType.LUNCH,
                hadLunch = true,
                isStanding = true,
                intervalsRemaining = uiState.value.numberOfIntervals,
                onSnooze = false
            )
        }
        setTimer()
        startTimer()
    }

    fun snoozeTimer() {
        _uiState.update { currentState ->
            currentState.copy(
                onSnooze = true,
                timerType =
                    if (_uiState.value.timerType == TimerType.SIT || _uiState.value.timerType == TimerType.STAND) {
                        TimerType.BREAK
                    } else {
                        if (_uiState.value.isStanding) TimerType.STAND else TimerType.SIT
                    }
            )
        }
        setTimer()
        startTimer()
    }

    fun updateIntervalsAtStart() {
        _uiState.update { currentState ->
            currentState.copy(
                intervalsRemaining = uiState.value.numberOfIntervals
            )
        }
    }

    private var timerHelper: TimerHelper? = null

    fun setTimer() {
        timerRepository.cancelWorker(TIMER_FINISHED_TAG)
        timerRepository.stopTimerFinishedNotification()
        val duration: Int =
            if (_uiState.value.onSnooze) {
                uiState.value.snoozeLength.toInt() * 60
            } else when (_uiState.value.timerType) {
                TimerType.STAND, TimerType.SIT -> uiState.value.intervalLength.toInt() * 60
                TimerType.BREAK -> uiState.value.breakLength.toInt() * 60
                TimerType.LUNCH -> uiState.value.lunchLength.toInt() * 60
            }

        timerHelper = object : TimerHelper(durationInSeconds = duration) {
            override fun onTimerTick(remainingTime: Int) {
                _uiState.update { currentState ->
                    var minutes = (remainingTime / 60).toString()
                    var seconds = (remainingTime % 60).toString()
                    val timeToBreak = ceil((remainingTime.toFloat() / 60 + (uiState.value.intervalsRemaining - 1) * uiState.value.intervalLength)).toInt().toString()
                    if (minutes.length == 1) minutes = "0$minutes"
                    if (seconds.length == 1) seconds = "0$seconds"

                    currentState.copy(
                        timeRemainingInSeconds = remainingTime,
                        minutesRemaining = minutes,
                        secondsRemaining = seconds,
                        timeToBreak = timeToBreak,
                    )
                }
            }

            override fun onTimerFinish() {
                if (_uiState.value.timerType == TimerType.SIT || _uiState.value.timerType == TimerType.STAND) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            intervalsRemaining = uiState.value.intervalsRemaining - 1,
                            isStanding = !uiState.value.isStanding,
                            timerType = if (_uiState.value.isStanding) TimerType.SIT else TimerType.STAND,
                            onSnooze = false
                        )
                    }
                    if (_uiState.value.intervalsRemaining < 1) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                timerType = TimerType.BREAK,
                                intervalsRemaining = uiState.value.numberOfIntervals,
                                isTimeToScanNFC = true,
                            )
                        }
                    }
                } else if (_uiState.value.timerType == TimerType.BREAK || _uiState.value.timerType == TimerType.LUNCH) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            timerType = if (_uiState.value.isStanding) TimerType.STAND else TimerType.SIT,
                            isTimeToScanNFC = true,
                            onSnooze = false
                        )
                    }
                }
                val type = _uiState.value.timerType.name
                timerRepository.startTimerFinishedNotification(type, _uiState.value.isSilentModeOn)
                navigateToPage("Alarm")
                endTimer()
            }
        }
    }

    fun startTimer() {
        timerHelper?.start()
        val type = _uiState.value.timerType.name
        _uiState.update { currentState ->
            currentState.copy(
                minutesRemaining =
                if (_uiState.value.onSnooze) {
                    uiState.value.snoozeLength.toInt().toString()
                } else when (_uiState.value.timerType) {
                    TimerType.SIT, TimerType.STAND -> uiState.value.intervalLength.toInt().toString()
                    TimerType.BREAK -> uiState.value.breakLength.toInt().toString()
                    TimerType.LUNCH -> uiState.value.lunchLength.toInt().toString()
                },
                secondsRemaining = "00",
                timeToBreak = (uiState.value.intervalLength * uiState.value.intervalsRemaining).toInt().toString(),
                isTimeToScanNFC = false
            )
        }
        timerRepository.startTimerRunningNotification(type)
    }

    // fully resets the timer and UiState when end day is pressed
    fun resetTimer() {
        timerHelper?.reset()
        _uiState.update { currentState ->
            currentState.copy(
                minutesRemaining = uiState.value.intervalLength.toInt().toString(),
                secondsRemaining = "00",
                intervalsRemaining = uiState.value.numberOfIntervals,
                isTimeToScanNFC = false,
                hadLunch = false,
                isStanding = true,
                timerType = TimerType.STAND,
                onSnooze = false
            )
        }
        timerRepository.cancelWorker(TIMER_RUNNING_TAG)
        timerRepository.cancelWorker(TIMER_FINISHED_TAG)
        timerRepository.stopTimerRunningNotification()
        timerRepository.stopTimerFinishedNotification()
    }

    // for use with the end of timer running
    fun endTimer() {
        timerHelper?.reset()
        timerRepository.cancelWorker(TIMER_RUNNING_TAG)
        timerRepository.stopTimerRunningNotification()
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

    // To update the time on the start screen
    init {
        CoroutineScope(Dispatchers.IO).launch {
            updateTime()
            delay(60000 - System.currentTimeMillis() % 60000)
            while (true) {
                updateTime()
                delay(60000)
            }
        }
    }

    // Utility function to get the formatted current time
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTime(){
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        _uiState.update { currentState ->
            currentState.copy(currentTime = currentTime)
        }
    }


}