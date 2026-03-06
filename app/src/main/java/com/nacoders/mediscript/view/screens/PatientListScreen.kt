package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nacoders.mediscript.models.Patient
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.view.components.PatientCard
import com.nacoders.mediscript.view.components.PatientSearchBar

@Composable
fun PatientListScreen(navController: NavController) {

    val patients = listOf(
        Patient("Rahim", "45", "01700000000"),
        Patient("Karim", "32", "01800000000"),
        Patient("Jamal", "50", "01900000000")
    )

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
                        age = "45",
                        phone = "01700000000"
                    ) {
                        val id=patient.phone
                        navController.navigate("${NavRoutes.PRESCRIPTION_DETAIL}/$id")
                    }
                }
            }
        }
    }
}