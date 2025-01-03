package com.example.sitstandtimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sitstandtimer.data.TimerType

@Composable
fun TimerRunningScreen(
    minutesRemaining: String,
    secondsRemaining: String,
    onLunchButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onSwapButtonClicked: () -> Unit,
    onEndButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    hadLunch: Boolean,
    timerType: TimerType,
    timeToBreak: String,
    modifier: Modifier = Modifier
) {
        Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(160.dp))
        Text(
            text = "$minutesRemaining:$secondsRemaining",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = when (timerType) {
                TimerType.BREAK -> "You should be on a break"
                TimerType.SIT -> "You should be sitting"
                TimerType.STAND -> "You should be standing"
                TimerType.LUNCH -> "You should be on lunch"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (timerType == TimerType.SIT || timerType == TimerType.STAND) "Time to next break: $timeToBreak min" else ""
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onSwapButtonClicked,
            modifier = Modifier
        ) {
            Text(
                text = when (timerType) {
                    TimerType.BREAK -> "Return early"
                    TimerType.SIT -> "Stand early"
                    TimerType.STAND -> "Sit early"
                    TimerType.LUNCH -> "Return early"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (timerType == TimerType.SIT || timerType == TimerType.STAND) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.width(280.dp)
            ) {
                if (!hadLunch) {
                    Button(
                        onClick = onLunchButtonClicked,
                        modifier = Modifier
                    ) {
                        Text(
                            text = "Lunch",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Button(
                    onClick = onPauseButtonClicked,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Break",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else { Spacer(modifier = Modifier.height(48.dp))}
        Spacer(modifier = Modifier.height(84.dp))
        Button(
            onClick = onEndButtonClicked,
            modifier = Modifier
        ) {
            Text(
                text = "End day",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = onSettingsButtonClicked
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

//@Preview (showBackground = true)
//@Composable
//fun TimerRunningScreenPreview() {
//    SitStandTimerTheme(){
//        TimerRunningScreen(
//            onLunchButtonClicked = {},
//            onPauseButtonClicked = {},
//            onSwapButtonClicked = {},
//            onEndButtonClicked = {},
//            onSettingsButtonClicked = {},
//            isStanding = false,
//            hadLunch = false,
//            onBreak = false,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//        )
//    }
//}