package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.models.PrescriptionItem
import com.nacoders.mediscript.ui.theme.* // Custom palette
import com.nacoders.mediscript.util.PdfGenerator
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionDetailScreen(navController: NavController, patientId: String) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val currentDoctorId = remember {
        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }
    val viewModel: PatientViewModel = viewModel(
        factory = PatientViewmodelFactory(database.patientDao(), database.prescriptionDao(), currentDoctorId)
    )
    val patient by viewModel.patient.collectAsState()
    val prescription by viewModel.prescription.collectAsState()

    LaunchedEffect(patientId) {
        if (patientId.isNotBlank()) viewModel.loadPatientByPhone(patientId)
    }

    Scaffold(
        containerColor = SurfaceWhite,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Prescription Detail", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceWhite)
            )
        }
    ) { padding ->
        if (patient == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MedicalBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // --- Section 1: Patient Header ---
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    PatientInfoSection(patient)
                }

                // --- Section 2: Medicine List ---
                item {
                    MedicineListSection(prescription)
                }

                // --- Section 3: Clinical Notes ---
                item {
                    NotesSection(prescription)
                }

                // --- Section 4: Export Actions ---
                item {
                    ActionButtons(patient!!, prescription)
                }
            }
        }
    }
}

@Composable
fun PatientInfoSection(patient: PatientEntity?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape).background(MedicalBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Person, contentDescription = null, tint = MedicalBlue)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = patient?.name ?: "Unknown", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = "${patient?.age} yrs • ${patient?.gender}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text(text = patient?.phone ?: "", style = MaterialTheme.typography.bodySmall, color = MedicalBlue)
            }
        }
    }
}

@Composable
fun MedicineListSection(prescription: PrescriptionEntity?) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Medication, contentDescription = null, tint = MedicalBlue, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Prescribed Medications", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        }

        val formattedDate = prescription?.let { formatPrescriptionTime(it.date) } ?: "Date not available"
        Text(text = "Issued on: $formattedDate", style = MaterialTheme.typography.labelSmall, color = TextSecondary)

        prescription?.medicineList?.forEach { medicine ->
            MedicineDetailCard(medicine)
        }
    }
}

@Composable
fun MedicineDetailCard(medicine: PrescriptionItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = medicine.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Surface(
                    color = MedicalBlueLight.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = formatFrequency(medicine),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MedicalBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoBadge(label = "Dosage", value = medicine.dosage)
                InfoBadge(label = "Duration", value = medicine.duration)
            }
        }
    }
}

@Composable
fun InfoBadge(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
    }
}

@Composable
fun NotesSection(prescription: PrescriptionEntity?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MedicalBlue.copy(alpha = 0.03f)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MedicalBlue.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.HistoryEdu, contentDescription = null, tint = MedicalBlue, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Doctor's Observations", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MedicalBlue)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = prescription?.notes ?: "No additional notes provided.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ActionButtons(patient: PatientEntity, prescription: PrescriptionEntity?) {
    val context = LocalContext.current
    val generator = remember { PdfGenerator(context) }
    fun sharePdf(file: java.io.File) {
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Matches your Manifest authority
            file
        )

        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(android.content.Intent.EXTRA_STREAM, uri)
            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(android.content.Intent.createChooser(intent, "Share Prescription"))
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = { generator.generateAndSavePdf(patient, prescription) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue)
        ) {
            Icon(Icons.Rounded.PictureAsPdf, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Generate Official PDF", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = {
                val pdfFile = generator.generateAndSavePdf(patient, prescription)

                if (pdfFile != null && pdfFile.exists()) {
                    sharePdf(pdfFile)
                } else {
                    //any error
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MedicalBlue)
        ) {
            Icon(Icons.Rounded.IosShare, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Share with Patient", color = MedicalBlue, fontWeight = FontWeight.SemiBold)
        }
    }
}

// Helper functions (formatted for the new UI)
fun formatFrequency(item: PrescriptionItem): String {
    val times = mutableListOf<String>()
    if (item.isMorning) times.add("Morning")
    if (item.isAfternoon) times.add("Afternoon")
    if (item.isNight) times.add("Night")
    return if (times.isEmpty()) "As Needed" else times.joinToString(" + ")
}

fun formatPrescriptionTime(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter = DateTimeFormatter.ofPattern("h:mm a, d MMM yyyy", Locale.ENGLISH)
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}