package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.view.components.PatientCard
import com.nacoders.mediscript.view.components.PatientSearchBar
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory

@Composable
fun PatientListScreen(
    navController: NavController,
) {

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
                    navController.navigate(NavRoutes.ADD_PATIENT)
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

            PatientSearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(patients) { patient ->

                    PatientCard(
                        name = patient.name,
                        age = patient.age,
                        phone = patient.phone,
                        onLeftClick = {
                            val id = patient.phone
                            navController.navigate("${NavRoutes.PATIENT_DETAIL}/$id")
                        },
                        onRightClick = {
                            val id = patient.phone
                            navController.navigate("${NavRoutes.CREATE_PRESCRIPTION}?id=$id")
                        }
                    )
                }
            }
        }
    }
}