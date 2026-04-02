package com.nacoders.mediscript.view.screens

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.data.local.repo.MedicineRepository
import com.nacoders.mediscript.models.PrescriptionItem
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.ui.theme.CardBackground
import com.nacoders.mediscript.ui.theme.DividerColor
import com.nacoders.mediscript.ui.theme.MedicalBlue
import com.nacoders.mediscript.ui.theme.MedicalBlueLight
import com.nacoders.mediscript.ui.theme.SurfaceWhite
import com.nacoders.mediscript.ui.theme.TextSecondary
import com.nacoders.mediscript.utils.parsePrescriptionVoice
import com.nacoders.mediscript.view.components.AddMedicineButton
import com.nacoders.mediscript.view.components.MedicineInputSection
import com.nacoders.mediscript.view.components.PatientInfoCard
import com.nacoders.mediscript.view.components.PrescriptionPreviewCard
import com.nacoders.mediscript.viewmodel.CreatePrescriptionViewModel
import com.nacoders.mediscript.viewmodel.CreatePrescriptionViewModelFactory
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.Manifest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePrescriptionScreen(navController: NavController, patientId: String?) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "Microphone permission is required for dictation", Toast.LENGTH_SHORT).show()
            }
        }
    )
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    // State
    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var isMorning by remember { mutableStateOf(false) }
    var isAfternoon by remember { mutableStateOf(false) }
    var isNight by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }
    var medicines by remember { mutableStateOf(listOf<PrescriptionItem>()) }

    // Logic Setup (Simplified for UI focus)
    val database = AppDatabase.getInstance(context)
    val repository = MedicineRepository(database.medicineDao())
    val currentDoctorId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val viewModel: CreatePrescriptionViewModel = viewModel(factory = CreatePrescriptionViewModelFactory(repository, database.prescriptionDao()))
    val viewModel2: PatientViewModel = viewModel(factory = PatientViewmodelFactory(database.patientDao(), database.prescriptionDao(), currentDoctorId))
    val suggestions by viewModel.suggestions.collectAsState()
    val patient by viewModel2.patient.collectAsState()

    val voiceRecognizer = rememberVoiceRecognizer { result ->
        parsePrescriptionVoice(result, { medicineName = it }, { dosage = it }, { duration = it },
            { m, a, n -> isMorning = m; isAfternoon = a; isNight = n })
    }

    LaunchedEffect(patientId) { if (!patientId.isNullOrBlank()) viewModel2.loadPatientByPhone(patientId) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New Prescription", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceWhite)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = SurfaceWhite
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- Section 1: Patient Context ---
            item {
                Spacer(modifier = Modifier.height(8.dp))
                PatientInfoCard(patient, navController)
            }

            // --- Section 2: Medicine Entry Work-Zone ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Medication, contentDescription = null, tint = MedicalBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Medication", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        MedicineInputSection(
                            medicineName = medicineName,
                            dosage = dosage,
                            duration = duration,
                            suggestions = suggestions,
                            isMorning = isMorning, isAfternoon = isAfternoon, isNight = isNight,
                            onMorningChange = { isMorning = it }, onAfternoonChange = { isAfternoon = it }, onNightChange = { isNight = it },
                            onMedicineChange = { medicineName = it; viewModel.updateQuery(it) },
                            onSuggestionClick = { m -> medicineName = "${m.name} ${m.strength}"; viewModel.updateQuery("") },
                            onDosageChange = { dosage = it }, onDurationChange = { duration = it },
                            onFocusChanged = {}
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Dictation Feature
                        Button(
                            onClick = {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                }
                                voiceRecognizer.startListening(intent)
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlueLight.copy(alpha = 0.15f), contentColor = MedicalBlue),
                            shape = RoundedCornerShape(12.dp),
                            elevation = null
                        ) {
                            Icon(Icons.Rounded.Mic, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Quick Dictate", fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        AddMedicineButton(
                            onClick = {
                                if (medicineName.isNotBlank()) {
                                    medicines = medicines + PrescriptionItem(medicineName, dosage, duration, isMorning, isAfternoon, isNight)
                                    medicineName = ""; dosage = ""; duration = ""
                                    isMorning = false; isAfternoon = false; isNight = false
                                }
                            }
                        )
                    }
                }
            }

            // --- Section 3: Clinical Notes ---
            item {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Clinical Notes / Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = CardBackground,
                        focusedContainerColor = CardBackground,
                        unfocusedBorderColor = DividerColor
                    )
                )
            }

            // --- Section 4: Live Preview ---
            item {
                Text("Prescription Summary", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                PrescriptionPreviewCard(medicines)
            }

            // --- Section 5: Finalize Action ---
            item {
                Button(
                    onClick = {
                        if (patient != null && medicines.isNotEmpty()) {
                            val newPrescription = PrescriptionEntity(
                                doctorId = currentDoctorId,
                                patientPhone = patient!!.phone,
                                prescriptionNumber = "Pres-${System.currentTimeMillis()}",
                                notes = notes,
                                medicineList = medicines,
                                isSynced = false
                            )
                            viewModel.savePrescription(context, newPrescription)
                            scope.launch {
                                snackbarHostState.showSnackbar("Prescription Created")
                                delay(500)
                                navController.navigate(NavRoutes.PRESCRIPTION_LIST) {
                                    popUpTo(NavRoutes.CREATE_PRESCRIPTION) { inclusive = true }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(58.dp).padding(bottom = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue)
                ) {
                    Icon(Icons.Rounded.CheckCircle, null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Finalize & Save", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
@Composable
fun rememberVoiceRecognizer(onResult: (String) -> Unit): SpeechRecognizer {
    val context = LocalContext.current
    val recognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    DisposableEffect(Unit) {
        val listener = object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                data?.firstOrNull()?.let { onResult(it) }
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        recognizer.setRecognitionListener(listener)
        onDispose { recognizer.destroy() }
    }
    return recognizer
}