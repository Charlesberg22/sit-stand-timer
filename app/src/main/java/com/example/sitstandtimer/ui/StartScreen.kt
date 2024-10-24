package com.example.sitstandtimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(
    onStartButtonClicked: () -> Unit,
    onSwitchBetweenSitAndStand: (Boolean) -> Unit,
    onSwitchNfcOff: (Boolean) -> Unit,
    onSwitchBetweenSilentAndNoisy: (Boolean) -> Unit,
    onSettingsButtonClicked: () -> Unit,
    isStanding: Boolean,
    isNfcOn: Boolean,
    isSilentModeOn: Boolean,
    currentTime: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(160.dp))
        Text(text = currentTime, style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = onStartButtonClicked,
            modifier = Modifier
                .width(196.dp)
            ) {
            Text(text = "Start your day", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .width(220.dp)
        ) {
            Text(
                text = "Sitting",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Switch(
                checked = isStanding,
                onCheckedChange = onSwitchBetweenSitAndStand,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Standing",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.width(220.dp)
        ) {
            Text(
                text = "Use NFC",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Switch(
                checked = isNfcOn,
                onCheckedChange = onSwitchNfcOff,
                thumbContent = if (isNfcOn) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.width(220.dp)
        ) {
            Text(
                text = "Silent",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Switch(
                checked = isSilentModeOn,
                onCheckedChange = onSwitchBetweenSilentAndNoisy,
                thumbContent = if (isSilentModeOn) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
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
//fun StartScreenPreview() {
//    SitStandTimerTheme{
//        StartScreen(
//            onStartButtonClicked = {},
//            onSwitchBetweenSitAndStand = {},
//            onSettingsButtonClicked = {},
//            isStanding = true,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//        )
//    }
//}