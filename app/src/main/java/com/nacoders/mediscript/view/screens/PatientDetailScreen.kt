package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PatientDetailScreen(navController: NavController) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Patient Details",
                style = MaterialTheme.typography.headlineMedium
            )

            Text("Name: Rahim Ahmed")
            Text("Age: 45")
            Text("Gender: Male")
            Text("Phone: 01700000000")
            Text("Address: Chattogram, Bangladesh")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Prescription")
            }
        }
    }
}