package com.example.sitstandtimer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sitstandtimer.ui.theme.SitStandTimerTheme
import com.example.sitstandtimer.utils.RequestPermissions

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { deepLinkUri ->
            val path = deepLinkUri.path

            when (path) {
                "/Alarm" -> navController.navigate("Alarm")
                "/Timer" -> navController.navigate("Timer")
            }
        }
    }
}
