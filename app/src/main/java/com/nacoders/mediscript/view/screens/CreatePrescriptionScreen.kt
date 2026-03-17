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
    var notes by remember { mutableStateOf("") }

    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val repository = MedicineRepository(database.medicineDao())

    val viewModel: CreatePrescriptionViewModel = viewModel(
        factory = CreatePrescriptionViewModelFactory(repository)
    )
    val suggestions by viewModel.suggestions.collectAsState()

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

                onMedicineChange = {
                    medicineName = it
                    viewModel.updateQuery(it)
                },

                onSuggestionClick = {
                    medicineName = it.name?: ""
                },

                onDosageChange = { dosage = it },
                onDurationChange = { duration = it },
                onNotesChange = { notes = it }
            )
        }

        item {
            AddMedicineButton()
        }

        item {
            PrescriptionPreviewCard()
        }
    }
}