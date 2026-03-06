package com.nacoders.mediscript

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nacoders.mediscript.navigation.AppNavGraph
import com.nacoders.mediscript.ui.theme.SmartPrescriptionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartPrescriptionTheme {
                AppNavGraph()
            }
        }
    }
}