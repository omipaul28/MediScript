package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory

@Composable
fun PrescriptionHistoryScreen(navController: NavController) {

    val context = LocalContext.current

    val db = AppDatabase.getInstance(context)
    val dao = db.patientDao()
    val prescriptionDao = db.prescriptionDao()

    val viewModel: PatientViewModel = viewModel(
        factory = PatientViewmodelFactory(dao, prescriptionDao)
    )


    val patients by viewModel.patients.collectAsState()

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


                items(patients) { patient ->

                    PrescriptionCard(
                        patient.name,
                        patient.age,
                        patient.phone,
                        onClick = {
                            val id = patient.phone
                            navController.navigate("${NavRoutes.PRESCRIPTION_DETAIL}/$id")

                        }
                    )
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
    name: String,
    age: String,
    phone: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp) // important for spacing
        ) {

            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Age: $age")
            Text("Phone: $phone")
        }
    }
}
