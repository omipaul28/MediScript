package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.ContactPage
import androidx.compose.material.icons.rounded.HistoryEdu
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.ui.theme.CardBackground
import com.nacoders.mediscript.ui.theme.ErrorRed
import com.nacoders.mediscript.ui.theme.MedicalBlue
import com.nacoders.mediscript.ui.theme.MedicalBlueLight
import com.nacoders.mediscript.ui.theme.SurfaceWhite
import com.nacoders.mediscript.ui.theme.TextPrimary
import com.nacoders.mediscript.ui.theme.TextSecondary
import com.nacoders.mediscript.viewmodel.AuthViewModel
import com.nacoders.mediscript.viewmodel.AuthViewModelFactory

@Composable
fun DashboardScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val doctorData by authViewModel.doctorProfile.collectAsState()

    Scaffold(
        containerColor = SurfaceWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- Doctor Profile Header ---
            DoctorHeader(
                name = doctorData?.name ?: "Doctor",
                hospital = doctorData?.hospital ?: "Medical Center",
                onLogout = { authViewModel.logout(navController) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Action Grid ---
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    DashboardActionCard(
                        title = "Add Patient",
                        subtitle = "New record",
                        icon = Icons.Rounded.PersonAdd,
                        onClick = { navController.navigate(NavRoutes.ADD_PATIENT) }
                    )
                }
                item {
                    DashboardActionCard(
                        title = "Prescription",
                        subtitle = "Create new",
                        icon = Icons.Rounded.HistoryEdu,
                        onClick = { navController.navigate(NavRoutes.CREATE_PRESCRIPTION) }
                    )
                }
                item {
                    DashboardActionCard(
                        title = "Patient List",
                        subtitle = "View all",
                        icon = Icons.Rounded.ContactPage,
                        onClick = { navController.navigate(NavRoutes.PATIENT_LIST) }
                    )
                }
                item {
                    DashboardActionCard(
                        title = "History",
                        subtitle = "Prescriptions",
                        icon = Icons.Rounded.Assignment,
                        onClick = { navController.navigate(NavRoutes.PRESCRIPTION_LIST) }
                    )
                }
            }
        }
    }
}

@Composable
fun DoctorHeader(name: String, hospital: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardBackground)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with initials or icon
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MedicalBlueLight.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = MedicalBlue,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Welcome,",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = "Dr. $name",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = hospital,
                style = MaterialTheme.typography.bodySmall,
                color = MedicalBlue,
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(
            onClick = onLogout,
            modifier = Modifier
                .clip(CircleShape)
                .background(ErrorRed.copy(alpha = 0.1f))
        ) {
            Icon(
                imageVector = Icons.Rounded.Logout,
                contentDescription = "Logout",
                tint = ErrorRed,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun DashboardActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MedicalBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MedicalBlue,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}