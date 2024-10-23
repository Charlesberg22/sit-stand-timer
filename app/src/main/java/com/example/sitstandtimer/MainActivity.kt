package com.example.sitstandtimer

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sitstandtimer.ui.theme.SitStandTimerTheme
import com.example.sitstandtimer.utils.INTENT_ACTION_NFC_READ
import com.example.sitstandtimer.utils.RequestPermissions
import com.example.sitstandtimer.utils.getParcelableCompatibility

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private var nfcAdapter : NfcAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            SitStandTimerTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    RequestPermissions()
                }
                TimerApp(navController = navController)
            }
        }
    }

    private fun enableNfcForegroundDispatch() {
        nfcAdapter?.let { adapter ->
            if (adapter.isEnabled) {
                val nfcIntentFilter = arrayOf(
                    IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
                    IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
                    IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
                )

                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        PendingIntent.FLAG_MUTABLE
                    )
                } else {
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                adapter.enableForegroundDispatch(
                    this, pendingIntent, nfcIntentFilter, null
                )
            }
        }
    }

    private fun disableNfcForegroundDispatch() {
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onResume() {
        super.onResume()
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        disableNfcForegroundDispatch()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // handle deep links from notifications
        intent.data?.let { deepLinkUri ->
            val path = deepLinkUri.path

            when (path) {
                "/Alarm" -> navController.navigate("Alarm")
                "/Timer" -> navController.navigate("Timer")
            }
        }

        // handle NFC tag scanning
        intent.let { nfcIntent ->
            sendBroadcast(Intent(INTENT_ACTION_NFC_READ).apply {
                putExtra(
                    NfcAdapter.EXTRA_TAG,
                    nfcIntent.getParcelableCompatibility(NfcAdapter.EXTRA_TAG, Tag::class.java)
                )
                setPackage(packageName)
            })
        }
    }
}
