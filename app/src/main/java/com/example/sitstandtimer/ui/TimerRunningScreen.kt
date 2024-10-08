package com.example.sitstandtimer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sitstandtimer.ui.theme.SitStandTimerTheme

@Composable
fun TimerRunningScreen(
    onLunchButtonClicked: () -> Unit,
    onSwapButtonClicked: () -> Unit,
    onEndButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    isStanding: Boolean,
    modifier: Modifier = Modifier
) {
    val timeToBreak = 30
    Column(
        modifier = modifier
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(160.dp))
        Text(text = "30:00", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "You should be ${if (isStanding) "standing" else "sitting"}",
            style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Time to next break: $timeToBreak min"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onSwapButtonClicked,
            modifier = Modifier
        ) {
            Text(
                text = "${if (isStanding) "Sit" else "Stand"} early",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
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
        Spacer(modifier = Modifier.height(84.dp))
        Button(
            onClick = onEndButtonClicked,
            modifier = Modifier
        ) {
            Text(
                text = "End day",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
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

@Preview (showBackground = true)
@Composable
fun TimerRunningScreenPreview() {
    SitStandTimerTheme(){
        TimerRunningScreen(
            onLunchButtonClicked = {},
            onSwapButtonClicked = {},
            onEndButtonClicked = {},
            onSettingsButtonClicked = {},
            isStanding = false,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}