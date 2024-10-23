package com.example.sitstandtimer.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

// credit: https://medium.com/asis-technologies/android-jetpack-compose-transmitting-nfc-read-data-to-screens-using-broadcast-d3ad3c568a5b

const val INTENT_ACTION_NFC_READ = "com.example.sitstandtimer.utils.INTENT_ACTION_NFC_READ"

@Composable
fun NfcBroadcastReceiver(
    onSuccess: (Tag) -> Unit
) {
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(onSuccess)

    DisposableEffect(context) {
        val intentFilter = IntentFilter(INTENT_ACTION_NFC_READ)
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                p1?.getParcelableCompatibility(NfcAdapter.EXTRA_TAG, Tag::class.java)?.let { tag ->
                    currentOnSystemEvent(tag)
                }
            }
        }
        ContextCompat.registerReceiver(
            context,
            broadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        onDispose {
            context.unregisterReceiver((broadcastReceiver))
        }
    }
}

internal fun <T : Parcelable> Intent.getParcelableCompatibility(key: String, type: Class<T>): T? {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key,type)
    } else {
        getParcelableExtra(key)
    }
}