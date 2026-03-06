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
import com.nacoders.mediscript.data.local.database.AppDatabase
import com.nacoders.mediscript.data.repository.MedicineRepository
import com.nacoders.mediscript.view.components.AddMedicineButton
import com.nacoders.mediscript.view.components.MedicineInputSection
import com.nacoders.mediscript.view.components.PatientInfoCard
import com.nacoders.mediscript.view.components.PrescriptionPreviewCard

@Composable
fun CreatePrescriptionScreen(navController: NavController) {

    var medicineName by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val context = LocalContext.current

    val db = AppDatabase.getDatabase(context)

    val repository = MedicineRepository(db.medicineDao())

    val viewModel: MedicineViewModel = viewModel(
        factory = MedicineViewModelFactory(repository)
    )
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
                viewModel,
                medicineName,
                dosage,
                duration,
                notes,
                onMedicineChange = { medicineName = it },
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