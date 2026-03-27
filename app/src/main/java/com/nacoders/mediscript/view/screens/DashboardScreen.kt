package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.view.components.DashboardCard
import com.nacoders.mediscript.viewmodel.AuthViewModel
import com.nacoders.mediscript.viewmodel.AuthViewModelFactory


@Composable
fun DashboardScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {

        val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
        val authViewModel: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(context)
        )
        val doctorData by authViewModel.doctorProfile.collectAsState()
        DoctorHeader(name = doctorData?.name ?: "Doctor",
            hospital = doctorData?.hospital ?: "Medical Center",
            onLogout = { authViewModel.logout(navController) })

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                DashboardCard(
                    title = "Add Patient",
                    icon = Icons.Default.AddCircle
                ) {
                    navController.navigate(NavRoutes.ADD_PATIENT)
                }
            }

            item {
                DashboardCard(
                    title = "New Prescription",
                    icon = Icons.Default.Edit
                ) {
                    navController.navigate(NavRoutes.CREATE_PRESCRIPTION)
                }
            }

            item {
                DashboardCard(
                    title = "Patient List",
                    icon = Icons.Default.AccountBox
                ) {
                    navController.navigate(NavRoutes.PATIENT_LIST)
                }
            }

            item {
                DashboardCard(
                    title = "Prescription List",
                    icon = Icons.Default.MailOutline
                ) {
                    navController.navigate(NavRoutes.PRESCRIPTION_LIST)
                }
            }
        }
    }
}

@Composable
fun DoctorHeader(name: String, hospital: String, onLogout: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(text = hospital, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onLogout) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}