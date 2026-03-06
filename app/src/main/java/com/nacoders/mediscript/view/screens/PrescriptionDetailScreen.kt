package com.nacoders.mediscript.view.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PrescriptionDetailScreen(
    navController: NavController,
    prescriptionId: String
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item { PatientInfoSection() }

        item { MedicineListSection() }

        item { NotesSection() }

        item { ActionButtons() }
    }
}

@Composable
fun PatientInfoSection() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = "Patient Information",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Name: Rahim Ahmed")
            Text("Age: 45")
            Text("Gender: Male")
            Text("Phone: 01700000000")
        }
    }
}

@Composable
fun MedicineListSection() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Medicines",
                style = MaterialTheme.typography.titleLarge
            )

            MedicineItem(
                name = "Napa 500mg",
                dosage = "1 tablet",
                frequency = "Morning + Night",
                duration = "5 days"
            )

            MedicineItem(
                name = "Omeprazole",
                dosage = "1 capsule",
                frequency = "Morning",
                duration = "7 days"
            )
        }
    }
}

@Composable
fun MedicineItem(
    name: String,
    dosage: String,
    frequency: String,
    duration: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Dosage: $dosage")
            Text("Frequency: $frequency")
            Text("Duration: $duration")
        }
    }
}

@Composable
fun NotesSection() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Doctor Notes",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Take medicine after food. Drink plenty of water."
            )
        }
    }
}

@Composable
fun ActionButtons() {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Icon(Icons.Default.ExitToApp, contentDescription = null)

            Spacer(modifier = Modifier.width(8.dp))

            Text("Generate PDF")
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Icon(Icons.Default.Share, contentDescription = null)

            Spacer(modifier = Modifier.width(8.dp))

            Text("Share Prescription")
        }
    }
}