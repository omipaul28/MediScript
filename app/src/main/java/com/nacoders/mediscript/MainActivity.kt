package com.nacoders.mediscript

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.repo.pullDataFromFirestore
import com.nacoders.mediscript.data.local.repo.syncMedicinesFromFirebase
import com.nacoders.mediscript.data.local.repo.uploadJsonToFirebase
import com.nacoders.mediscript.navigation.AppNavGraph
import com.nacoders.mediscript.ui.theme.SmartPrescriptionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getInstance(this)
        // Start listening for remote changes immediately
        pullDataFromFirestore(database)
        syncMedicinesFromFirebase(database)
        //uploadJsonToFirebase(context = this)//only first time
        setContent {
            SmartPrescriptionTheme {
                AppNavGraph()
            }
        }
    }
}
