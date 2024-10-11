package com.example.sitstandtimer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sitstandtimer.ui.AlarmScreen
import com.example.sitstandtimer.ui.SettingsScreen
import com.example.sitstandtimer.ui.StartScreen
import com.example.sitstandtimer.ui.TimerRunningScreen
import com.example.sitstandtimer.ui.TimerViewModel

enum class TimerAppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Timer(title = R.string.timer_runnning),
    Alarm(title = R.string.alarm),
    Settings(title = R.string.settings)
}

@Composable
fun TimerApp(
    viewModel: TimerViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = TimerAppScreen.valueOf(
        backStackEntry?.destination?.route ?: TimerAppScreen.Start.name
    )

    Scaffold { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = TimerAppScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = TimerAppScreen.Start.name){
                StartScreen(
                    onStartButtonClicked = {
                        /*TODO set up timer and viewmodel stuff*/
                        navController.navigate(TimerAppScreen.Timer.name)
                    },
                    onSwitchBetweenSitAndStand = {},
                    onSettingsButtonClicked = {
                        navController.navigate(TimerAppScreen.Settings.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = TimerAppScreen.Timer.name) {
                TimerRunningScreen(
                    onLunchButtonClicked = {},
                    onPauseButtonClicked = {},
                    onSwapButtonClicked = {},
                    onEndButtonClicked = {
                        /*TODO set up timer and viewmodel stuff*/
                        navController.popBackStack(TimerAppScreen.Start.name, inclusive = false)
                    },
                    onSettingsButtonClicked = {
                        navController.navigate(TimerAppScreen.Settings.name)
                        },
                    isStanding = false,
                    hadLunch = false,
                    onBreak = false,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = TimerAppScreen.Alarm.name) {
                AlarmScreen(
                    onPauseButtonClicked = {
                        /*TODO set up timer and viewmodel stuff*/
                        navController.popBackStack(TimerAppScreen.Timer.name, inclusive = false)
                    },
                    onAcknowledgeClicked = {
                        /*TODO set up timer and viewmodel stuff*/
                        navController.popBackStack(TimerAppScreen.Timer.name, inclusive = false)
                    },
                    onEndButtonClicked = {
                        /*TODO set up timer and viewmodel stuff*/
                        navController.popBackStack(TimerAppScreen.Start.name, inclusive = false)
                    },
                    isStanding = false,
                    isTimeToScanNfc = false,
                    onBreak = true,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = TimerAppScreen.Settings.name) {
                SettingsScreen(
                    onBackButtonClick = {navController.navigateUp()}
                )
            }
        }

    }
}