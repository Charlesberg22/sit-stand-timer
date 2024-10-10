package com.example.sitstandtimer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sitstandtimer.ui.AlarmScreen
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
    Scaffold { innerPadding ->
        AlarmScreen(
            onPauseButtonClicked = {},
            onAcknowledgeClicked = {},
            onEndButtonClicked = {},
            isStanding = false,
            isTimeToScanNfc = false,
            onBreak = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}