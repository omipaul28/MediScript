package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.data.local.repo.MedicineRepository
import com.nacoders.mediscript.models.PrescriptionItem
import com.nacoders.mediscript.view.components.AddMedicineButton
import com.nacoders.mediscript.view.components.MedicineInputSection
import com.nacoders.mediscript.view.components.PatientInfoCard
import com.nacoders.mediscript.view.components.PrescriptionPreviewCard
import com.nacoders.mediscript.viewmodel.CreatePrescriptionViewModel
import com.nacoders.mediscript.viewmodel.CreatePrescriptionViewModelFactory
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory

@Composable
fun CreatePrescriptionScreen(navController: NavController, patientId: String?) {

    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var isMorning by remember { mutableStateOf(false) }
    var isAfternoon by remember { mutableStateOf(false) }
    var isNight by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }
    var medicines by remember { mutableStateOf(listOf<PrescriptionItem>()) }
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val repository = MedicineRepository(database.medicineDao())
    val prescriptionDao = database.prescriptionDao()
    val dao = database.patientDao()
    val viewModel2: PatientViewModel = viewModel(
        factory = PatientViewmodelFactory(dao, prescriptionDao)
    )
    val viewModel: CreatePrescriptionViewModel = viewModel(
        factory = CreatePrescriptionViewModelFactory(repository, prescriptionDao)
    )
    val suggestions by viewModel.suggestions.collectAsState()
    var isMedicineFocused by remember { mutableStateOf(false) }
    val patient by viewModel2.patient.collectAsState()


    LaunchedEffect(patientId) {
        if (!patientId.isNullOrBlank()) { // Check for blank strings
            viewModel2.loadPatientByPhone(patientId)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            PatientInfoCard(patient, navController)
        }

        item {
            MedicineInputSection(
                medicineName = medicineName,
                dosage = dosage,
                duration = duration,
                suggestions = suggestions,

                isMorning = isMorning,
                isAfternoon = isAfternoon,
                isNight = isNight,

                onMorningChange = { isMorning = it },
                onAfternoonChange = { isAfternoon = it },
                onNightChange = { isNight = it },

                onMedicineChange = {
                    medicineName = it
                    viewModel.updateQuery(it)
                },

                onSuggestionClick = {medicine ->
                    val fullName = "${medicine.name} ${medicine.strength} ${medicine.form}"
                    medicineName = fullName
                    viewModel.updateQuery("")
                    isMedicineFocused = false
                },

                onFocusChanged = {
                    isMedicineFocused = it
                },
                onDosageChange = { dosage = it },
                onDurationChange = { duration = it }
            )
        }

        item {
            AddMedicineButton(
                onClick = {
                    if (medicineName.isNotBlank()) {

                        medicines = medicines + PrescriptionItem(
                            name = medicineName,
                            dosage = dosage,
                            duration = duration,
                            isMorning = isMorning,
                            isAfternoon = isAfternoon,
                            isNight = isNight,
                            notes = notes
                        )
                        isMorning = false
                        isAfternoon = false
                        isNight = false
                        medicineName = ""
                        dosage = ""
                        duration = ""
                        notes = ""
                    }
                }
            )
        }
        item {
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }


        item {
            PrescriptionPreviewCard(medicines)
        }
        item {
            Button(
                onClick = {
                    if (patient != null && medicines.isNotEmpty()) {
                        val newPrescription = PrescriptionEntity(
                            patientPhone = patient!!.phone,
                            prescriptionNumber = "Pres-${System.currentTimeMillis()}",
                            notes = notes,
                            medicineList = medicines
                        )
                        // Call viewModel to save to Room
                        viewModel.savePrescription(newPrescription)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {

                Icon(Icons.Default.Done, null)

                Spacer(modifier = Modifier.width(8.dp))

                Text("Create Prescription")
            }
        }
    }
}