package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nacoders.mediscript.models.Prescription
import com.nacoders.mediscript.navigation.NavRoutes

@Composable
fun PrescriptionHistoryScreen(navController: NavController) {

    val prescriptions = listOf(
        Prescription("1","Rahim Ahmed","12 Mar 2026"),
        Prescription("2","Karim Uddin","10 Mar 2026"),
        Prescription("3", "Jannat Begum", "08 Mar 2026")
    )

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoutes.CREATE_PRESCRIPTION)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            PrescriptionSearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(prescriptions) { prescription ->

                    PrescriptionCard(
                        prescription = prescription
                    ) {
                        navController.navigate(
                            NavRoutes.PRESCRIPTION_DETAIL + "/${prescription.id}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PrescriptionSearchBar() {

    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        label = { Text("Search Prescription") },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        singleLine = true
    )
}


@Composable
fun PrescriptionCard(
    prescription: Prescription,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),

        elevation = CardDefaults.cardElevation(4.dp),

        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = prescription.patientName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Date: ${prescription.date}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Tap to view prescription",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}