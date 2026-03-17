package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory

@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
fun PatientDetailScreen(navController: NavController, patientId: String) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val dao = database.patientDao()
    val prescriptionDao = database.prescriptionDao()

    val viewModel: PatientViewModel = viewModel(
        factory = PatientViewmodelFactory(dao, prescriptionDao)
    )

    val patient by viewModel.patient.collectAsState()

    // UI State for Editing
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(patientId) {
        if (patientId.isNotBlank()) {
            viewModel.loadPatientByPhone(patientId)
        }
    }

    LaunchedEffect(patient) {
        patient?.let {
            name = it.name
            age = it.age
            gender = it.gender
            address = it.address
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Profile") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isEditing) {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    patient?.let {
                                        val updated = it.copy(name = name, age = age, gender = gender, address = address)
                                        viewModel.updatePatient(updated)
                                        isEditing = false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("Save") }

                            OutlinedButton(onClick = { isEditing = false }, modifier = Modifier.weight(1f)) {
                                Text("Cancel")
                            }
                        }
                    } else {
                        Text("Name: $name", style = MaterialTheme.typography.headlineSmall)
                        Text("Age: $age")
                        Text("Gender: $gender")
                        Text("Address: $address")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = { isEditing = true }, modifier = Modifier.fillMaxWidth()) {
                            Text("Edit Patient")
                        }

                        Button(
                            onClick = {
                                patient?.let {
                                    viewModel.deletePatient(it)
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete Patient")
                        }
                    }
                }
            }
        }
    }
}