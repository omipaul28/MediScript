package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.ui.theme.* // Your custom palette: MedicalBlue, SurfaceWhite, etc.
import com.nacoders.mediscript.viewmodel.PatientViewModel
import com.nacoders.mediscript.viewmodel.PatientViewmodelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val currentDoctorId = remember {
        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    val viewModel: PatientViewModel = viewModel(
        factory = PatientViewmodelFactory(db.patientDao(), db.prescriptionDao(), currentDoctorId)
    )

    val patients by viewModel.patients.collectAsState()

    Scaffold(
        containerColor = SurfaceWhite, // Clinical background
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Prescription History",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceWhite)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(NavRoutes.CREATE_PRESCRIPTION) },
                containerColor = MedicalBlue, // Primary action color
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("New Prescription", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- History Summary ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Records",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary // Subtle label
                )
                Icon(
                    imageVector = Icons.Rounded.History,
                    contentDescription = null,
                    tint = MedicalBlue.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Prescription List ---
            if (patients.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No prescriptions found", color = TextSecondary)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Clear of FAB
                ) {
                    items(patients) { patient ->
                        PrescriptionCard(
                            name = patient.name,
                            age = patient.age,
                            phone = patient.phone,
                            onClick = {
                                navController.navigate("${NavRoutes.PRESCRIPTION_DETAIL}/${patient.phone}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PrescriptionCard(
    name: String,
    age: String,
    phone: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp), // Modern rounded corners
        colors = CardDefaults.cardColors(containerColor = CardBackground), //
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Icon Avatar ---
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MedicalBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Description,
                    contentDescription = null,
                    tint = MedicalBlue, // Themed icon
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- Patient Info ---
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary // High emphasis
                )
                Text(
                    text = "Age: $age • $phone",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary // Lower emphasis
                )
            }

            // --- Navigation Indicator ---
            Icon(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                contentDescription = null,
                tint = DividerColor, // Subtle hint
                modifier = Modifier
                    .size(16.dp)
                    .graphicsLayer(rotationZ = 180f) // Point right
            )
        }
    }
}