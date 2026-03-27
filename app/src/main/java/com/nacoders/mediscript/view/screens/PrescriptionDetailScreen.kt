package com.nacoders.mediscript.view.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.models.PrescriptionItem
import com.nacoders.mediscript.util.PdfGenerator
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PrescriptionDetailScreen(
    navController: NavController,
    patientId: String
) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val dao = database.patientDao()
    val prescriptionDao = database.prescriptionDao()
    val viewModel: PatientViewModel = viewModel(
        factory = PatientViewmodelFactory(dao, prescriptionDao)
    )
    val patient by viewModel.patient.collectAsState()
    val prescription by viewModel.prescription.collectAsState()

    LaunchedEffect(patientId) {
        if (!patientId.isNullOrBlank()) { // Check for blank strings
            viewModel.loadPatientByPhone(patientId)
        }
    }
    if (patient == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { PatientInfoSection(patient) }

            item { MedicineListSection(prescription) }

            item { NotesSection(prescription) }

            item {
                patient?.let { currentPatient ->
                    ActionButtons(
                        patient = currentPatient,
                        prescription = prescription
                    )
                }
            }
        }
    }
}

@Composable
fun PatientInfoSection(patient: PatientEntity?) {

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

            if (patient != null) {
                Text("Name: ${patient.name}")
                Text("Age: ${patient.age}")
                Text("Gender: ${patient.gender}")
                Text("Phone: ${patient.phone}")
            }
        }
    }
}

@Composable
fun MedicineListSection(prescription: PrescriptionEntity?) {

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
            val formattedDate = prescription?.let { formatPrescriptionTime(it.date) } ?: ""
            Text(
                text = "Last Prescribed: $formattedDate",
                style = MaterialTheme.typography.titleSmall
            )

            prescription?.medicineList?.forEach { medicine ->
                MedicineItem(
                    name = medicine.name,
                    dosage = medicine.dosage,
                    frequency = formatFrequency(medicine),
                    duration = medicine.duration
                )
            }
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
fun NotesSection(prescription: PrescriptionEntity?) {

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

            val note: String? = prescription?.notes
            Text(
                text = "$note"
            )
        }
    }
}

@Composable
fun ActionButtons(patient: PatientEntity, prescription: PrescriptionEntity?) {
    val context = LocalContext.current
    // Initialize the generator
    val generator = remember { PdfGenerator(context) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = {
                // Call the generation and saving logic
                generator.generateAndSavePdf(patient, prescription)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate PDF")
        }

        OutlinedButton(
            onClick = { /* Handle Share */ },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Share Prescription")
        }
    }
}
fun formatFrequency(item: PrescriptionItem): String {
    val times = mutableListOf<String>()
    if (item.isMorning) times.add("Morning")
    if (item.isAfternoon) times.add("Afternoon")
    if (item.isNight) times.add("Night")

    return if (times.isEmpty()) "As needed" else times.joinToString(" + ")
}
fun formatPrescriptionTime(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter = DateTimeFormatter.ofPattern("h:mm a, d MMMM, yyyy", Locale.ENGLISH)
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}