package com.example.sitstandtimer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sitstandtimer.data.TimerType
import com.example.sitstandtimer.utils.NfcBroadcastReceiver

@Composable
fun AlarmScreen(
    onPauseButtonClicked: () -> Unit,
    onAcknowledgeClicked: () -> Unit,
    onEndButtonClicked: () -> Unit,
    isTagStored: (String) -> Boolean,
    timerType: TimerType,
    isTimeToScanNfc: Boolean,
    isNfcOn: Boolean,
    modifier: Modifier = Modifier

) {
    var showNfcDialog by rememberSaveable { mutableStateOf(false) }

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
            onClick = {
                if (isTimeToScanNfc && isNfcOn) {
                    showNfcDialog = true
                } else {
                    onAcknowledgeClicked()
                }
            },
            shape = RoundedCornerShape(10),
            modifier = Modifier.height(96.dp).width(204.dp)
        ) {
            Text(
                text = if (isTimeToScanNfc && isNfcOn) "Scan ${if (timerType == TimerType.LUNCH || timerType == TimerType.BREAK) "remote NFC" else "desk NFC"}" else "Acknowledge",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        if (isTimeToScanNfc) {
            Button(
                onClick = onPauseButtonClicked,
                modifier = Modifier.width(204.dp)
            ) {
                Text(
                    text = if (timerType == TimerType.BREAK || timerType == TimerType.LUNCH) "I'm in the zone" else "Snooze",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Spacer(modifier = Modifier.height(48.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (false)/*(isTimeToScanNfc && (timerType == TimerType.SIT || timerType == TimerType.STAND))*/ {
            // TODO: update this button to have different length options, for now default
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
        if (showNfcDialog) {
            NfcDialogContent(
                onDialogDismiss = { showNfcDialog = false },
                onAcknowledgeClicked = onAcknowledgeClicked,
                isTagStored = isTagStored,
                location = if (timerType == TimerType.LUNCH || timerType == TimerType.BREAK) "remote" else "desk"
            )
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun NfcDialogContent(
    onDialogDismiss: () -> Unit,
    onAcknowledgeClicked: () -> Unit,
    isTagStored: (String) -> Boolean,
    location: String,
    modifier: Modifier = Modifier
) {
    var isTagCorrect: Boolean? by rememberSaveable { mutableStateOf(null) }

    NfcBroadcastReceiver { tag ->
        isTagCorrect = isTagStored(tag.id.toHexString())
    }
    AlertDialog(
        onDismissRequest = { onDialogDismiss() },
        confirmButton = {},
        title = { Text("Scan ${if (location == "remote") "remote" else "desk"} NFC Tag")},
        text = { Text(
            if (isTagCorrect != null) {
                if (isTagCorrect as Boolean) "This tag matches" else "This tag does not match"
            } else {
                ""
            }
        ) }
    )
    if (isTagCorrect != null) {
        if (isTagCorrect as Boolean) {
            onDialogDismiss()
            onAcknowledgeClicked()
        }
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