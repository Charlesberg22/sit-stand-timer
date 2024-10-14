package com.example.sitstandtimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sitstandtimer.data.TimerUiState

// TODO: implement NFC capabilities

@Composable
fun SettingsScreen(
    onBackButtonClick: () -> Unit,
    uiState: TimerUiState,
    viewModel: TimerViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onBackButtonClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "back button"
            )
        }
        HorizontalDivider(modifier = Modifier)
        SliderSetting(
            initialSliderValue = uiState.numberOfIntervals.toFloat() ,
            onSliderChange = {
                viewModel.updatePreferences(
                    numberOfIntervals = it.toInt(),
                )
            },
            settingText = "Number of intervals before break",
            valueRange = 0f..3f,
            steps = 2
        )
        SliderSetting(
            initialSliderValue = uiState.intervalLength,
            onSliderChange = {
                viewModel.updatePreferences(
                    intervalLength = it
                )
            },
            settingText = "Length of sit/stand interval in minutes",
            valueRange = 0f..60f,
            steps = 11
        )
        SliderSetting(
            initialSliderValue = uiState.breakLength,
            onSliderChange = {
                viewModel.updatePreferences(
                    breakLength = it
                )
            },
            settingText = "Length of break in minutes",
            valueRange = 0f..30f,
            steps = 5
        )
        SliderSetting(
            initialSliderValue = uiState.lunchLength,
            onSliderChange = {
                viewModel.updatePreferences(
                    lunchLength = it
                )
            },
            settingText = "Length of lunch in minutes",
            valueRange = 30f..90f,
            steps = 11
        )
        SliderSetting(
            initialSliderValue = uiState.snoozeLength,
            onSliderChange = {
                viewModel.updatePreferences(
                    snoozeLength = it
                )
            },
            settingText = "Snooze length in minutes",
            valueRange = 0f..30f,
            steps = 5
        )
        // TODO: add restore to defaults
        NfcSetting(
            description = "Set remote NFC card",
            onClick = {}
        )
        NfcSetting(
            description = "Set desk NFC card",
            onClick = {}
        )
    }
}

@Composable
fun SliderSetting(
    modifier: Modifier = Modifier,
    initialSliderValue: Float,
    onSliderChange: (Float) -> Unit,
    settingText: String,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
) {
    val sliderValue = initialSliderValue
    Text(
        text = settingText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Slider(
            value = sliderValue,
            onValueChange = onSliderChange,
            steps = steps,
            valueRange = valueRange,
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )
        Text(
            text = "%.0f".format(sliderValue),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .width(32.dp)
                .padding(end = 8.dp),
            textAlign = TextAlign.End,
        )
    }
    HorizontalDivider(modifier = Modifier)
}

@Composable
fun NfcSetting(
    modifier: Modifier = Modifier,
    description: String,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(end = 8.dp)
        ) {
            Text(text = "Set")
        }
    }
    HorizontalDivider(modifier = Modifier)
}

//@Preview(showBackground = true)
//@Composable
//fun SettingsScreenPreview() {
//    SitStandTimerTheme() {
//        SettingsScreen(
//            onBackButtonClick = {},
//            uiState = TimerUiState(),
//        )
//    }
//}
