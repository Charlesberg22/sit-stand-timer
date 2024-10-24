package com.example.sitstandtimer.utils

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions() {
    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    if (!notificationPermissionState.status.isGranted && notificationPermissionState.status.shouldShowRationale) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
            icon = { Icon(Icons.Filled.NotificationsActive, contentDescription = null) },
            title = {
                Text(
                    text = "Allow notifications permission",
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { notificationPermissionState.launchPermissionRequest() },
                ) {
                    Text("Confirm")
                }
            },
            onDismissRequest = {},
        )
    } else {
        LaunchedEffect(key1 = Unit) {
            this.launch {
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }
}