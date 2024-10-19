package com.example.sitstandtimer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sitstandtimer.data.TimerType

@Composable
fun AlarmScreen(
    onPauseButtonClicked: () -> Unit,
    onAcknowledgeClicked: () -> Unit,
    onEndButtonClicked: () -> Unit,
    timerType: TimerType,
    onBreak: Boolean,
    isTimeToScanNfc: Boolean,
    modifier: Modifier = Modifier

) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(160.dp))
        Text(
            text =
            when (timerType) {
                TimerType.STAND -> "Time to stand"
                TimerType.SIT -> "Time to sit"
                TimerType.BREAK -> "Time to take a break"
                TimerType.LUNCH -> "Time for lunch"
            },
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = onAcknowledgeClicked,
            shape = RoundedCornerShape(10),
            modifier = Modifier.height(96.dp).width(204.dp)
        ) {
            Text(
                text = if (isTimeToScanNfc) "Scan ${if (onBreak) "desk NFC" else "remote NFC"}" else "Acknowledge",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onPauseButtonClicked,
            modifier = Modifier.width(204.dp)
        ) {
            Text(
                text = if (onBreak) "Ceebs work" else "I'm in the zone",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (!onBreak) {
            // TODO: fix this button to have different length options, for now default
            Button(
                onClick = onPauseButtonClicked,
                modifier = Modifier.width(204.dp)
            ) {
                Text(
                    text = "I'm in a meeting",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Spacer(modifier = Modifier.height(48.dp))
        }
        Spacer(modifier = Modifier.height(68.dp))
        Button(
            onClick = onEndButtonClicked,
            modifier = Modifier
        ) {
            Text(
                text = "End day",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

//@Preview (showBackground = true)
//@Composable
//fun AlarmScreenPreview() {
//    SitStandTimerTheme(){
//        AlarmScreen(
//            onPauseButtonClicked = {},
//            onAcknowledgeClicked = {},
//            onEndButtonClicked = {},
//            isStanding = false,
//            isTimeToScanNfc = false,
//            onBreak = true,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp)
//        )
//    }
//}