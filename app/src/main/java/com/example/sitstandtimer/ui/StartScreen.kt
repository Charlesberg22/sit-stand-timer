package com.example.sitstandtimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sitstandtimer.ui.theme.SitStandTimerTheme

@Composable
fun StartScreen(
    onStartButtonClicked: () -> Unit,
    onSwitchBetweenSitAndStand: (Boolean) -> Unit,
    onSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(192.dp))
        Text(text = "09:00", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(80.dp))
        Button(
            onClick = onStartButtonClicked,
            modifier = Modifier.width(192.dp)
            ) {
            Text(text = "Start your day", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .width(160.dp)
        ) {
            Text(
                text = "Sit",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Switch(
                checked = true,
                onCheckedChange = onSwitchBetweenSitAndStand,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Stand",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = onSettingsButtonClicked
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview (showBackground = true)
@Composable
fun StartScreenPreview() {
    SitStandTimerTheme{
        StartScreen(
            onStartButtonClicked = {},
            onSwitchBetweenSitAndStand = {},
            onSettingsButtonClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}