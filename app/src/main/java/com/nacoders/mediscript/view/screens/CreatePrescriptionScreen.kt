package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.repo.MedicineRepository
import com.nacoders.mediscript.models.PrescriptionItem
import com.nacoders.mediscript.view.components.AddMedicineButton
import com.nacoders.mediscript.view.components.MedicineInputSection
import com.nacoders.mediscript.view.components.PatientInfoCard
import com.nacoders.mediscript.view.components.PrescriptionPreviewCard
import com.nacoders.mediscript.viewmodel.CreatePrescriptionViewModel
import com.nacoders.mediscript.viewmodel.CreatePrescriptionViewModelFactory

@Composable
fun CreatePrescriptionScreen(navController: NavController) {

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

    val viewModel: CreatePrescriptionViewModel = viewModel(
        factory = CreatePrescriptionViewModelFactory(repository)
    )
    val suggestions by viewModel.suggestions.collectAsState()
    var isMedicineFocused by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            PatientInfoCard()
        }

        item {
            MedicineInputSection(
                medicineName = medicineName,
                dosage = dosage,
                duration = duration,
                notes = notes,
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

                onSuggestionClick = {
                    medicineName = (it.name +" "+ it.strength +" "+ it.form)
                    isMedicineFocused = false
                },

                onFocusChanged = {
                    isMedicineFocused = it
                },
                onDosageChange = { dosage = it },
                onDurationChange = { duration = it },
                onNotesChange = { notes = it }
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
            PrescriptionPreviewCard(medicines)
        }
    }
}