package com.example.sitstandtimer

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.sitstandtimer.data.NfcTagLocation
import com.example.sitstandtimer.data.TimerType
import com.example.sitstandtimer.ui.AlarmScreen
import com.example.sitstandtimer.ui.SettingsScreen
import com.example.sitstandtimer.ui.StartScreen
import com.example.sitstandtimer.ui.TimerRunningScreen
import com.example.sitstandtimer.ui.TimerViewModel

enum class TimerAppScreen(val deepLink: NavDeepLink) {
    Start(navDeepLink { uriPattern = "https://www.sitstandtimer.com/Start" }),
    Timer(navDeepLink { uriPattern = "https://www.sitstandtimer.com/Timer" }),
    Alarm(navDeepLink { uriPattern = "https://www.sitstandtimer.com/Alarm" }),
    Settings(navDeepLink { uriPattern = "https://www.sitstandtimer.com/Settings" })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimerApp(
    viewModel: TimerViewModel = viewModel(
        factory = TimerViewModel.Factory
    ),
    navController: NavHostController
) {
    // for navigation by functions in the viewmodel
    val navigateTo by viewModel.navigateTo.collectAsState()

    // Observe navigation state and trigger navigation when not null
    LaunchedEffect(navigateTo) {
        navigateTo?.let { destination ->
            navController.navigate(destination)
            viewModel.resetNavigation() // make navigateTo null, so navigation stops
        }
    }

    Scaffold { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = TimerAppScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = TimerAppScreen.Start.name,
                deepLinks = listOf(TimerAppScreen.Start.deepLink)
                ){
                StartScreen(
                    onStartButtonClicked = {
                        viewModel.updateIntervalsAtStart()
                        viewModel.setTimer()
                        viewModel.startTimer()
                        navController.navigate(TimerAppScreen.Timer.name)
                    },
                    onSwitchBetweenSitAndStand = {viewModel.setStandingOrSitting()},
                    onSwitchNfcOff = {viewModel.switchNfcOnAndOff()},
                    onSwitchBetweenSilentAndNoisy = {viewModel.setSilentOrNoisy()},
                    onSettingsButtonClicked = {
                        navController.navigate(TimerAppScreen.Settings.name)
                    },
                    isStanding = uiState.isStanding,
                    isNfcOn = uiState.isNfcOn,
                    isSilentModeOn = uiState.isSilentModeOn,
                    currentTime = uiState.currentTime,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(
                route = TimerAppScreen.Timer.name,
                deepLinks = listOf(TimerAppScreen.Timer.deepLink)
            ) {
                BackHandler(true) {
                    // do nothing when back button pressed
                }
                TimerRunningScreen(
                    minutesRemaining = uiState.minutesRemaining,
                    secondsRemaining = uiState.secondsRemaining,
                    onLunchButtonClicked = {
                        viewModel.startLunch()
                    },
                    onPauseButtonClicked = {
                        viewModel.startManualBreak()
                    },
                    onSwapButtonClicked = {
                        viewModel.swapTimerType()
                    },
                    onEndButtonClicked = {
                        viewModel.resetTimer()
                        navController.popBackStack(TimerAppScreen.Start.name, inclusive = false)
                    },
                    onSettingsButtonClicked = {
                        navController.navigate(TimerAppScreen.Settings.name)
                    },
                    hadLunch = uiState.hadLunch,
                    timerType = uiState.timerType,
                    timeToBreak = uiState.timeToBreak,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(
                route = TimerAppScreen.Alarm.name,
                deepLinks = listOf(TimerAppScreen.Alarm.deepLink)
                ) {
                BackHandler(true) {
                    // do nothing when back button pressed
                }
                AlarmScreen(
                    onPauseButtonClicked = {
                        viewModel.snoozeTimer()
                        navController.popBackStack(TimerAppScreen.Timer.name, inclusive = false)
                    },
                    onAcknowledgeClicked = {
                        viewModel.setTimer()
                        viewModel.startTimer()
                        navController.popBackStack(TimerAppScreen.Timer.name, inclusive = false)
                    },
                    onEndButtonClicked = {
                        viewModel.resetTimer()
                        navController.popBackStack(TimerAppScreen.Start.name, inclusive = false)
                    },
                    isTagStored = {
                        viewModel.isTagStored(
                            tag = it,
                            location = if (uiState.timerType == TimerType.LUNCH || uiState.timerType == TimerType.BREAK) NfcTagLocation.REMOTE else NfcTagLocation.DESK,
                        )
                    },
                    timerType = uiState.timerType,
                    isTimeToScanNfc = uiState.isTimeToScanNFC,
                    isNfcOn = uiState.isNfcOn,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(
                route = TimerAppScreen.Settings.name,
                deepLinks = listOf(TimerAppScreen.Settings.deepLink)
            ) {
                SettingsScreen(
                    onBackButtonClick = {navController.navigateUp()},
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }

    }
}