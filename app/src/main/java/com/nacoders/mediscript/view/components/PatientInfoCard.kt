package com.nacoders.mediscript.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.navigation.NavRoutes
import com.nacoders.mediscript.ui.theme.* // Your MedicalBlue, CardBackground, etc.

@Composable
fun PatientInfoCard(patient: PatientEntity?, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { navController.navigate(NavRoutes.PATIENT_LIST) },
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Avatar Section ---
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (patient != null) MedicalBlue.copy(alpha = 0.1f)
                        else WarningOrange.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (patient != null) Icons.Rounded.Person else Icons.Rounded.PersonAdd,
                    contentDescription = null,
                    tint = if (patient != null) MedicalBlue else WarningOrange,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- Info Section ---
            Column(modifier = Modifier.weight(1f)) {
                if (patient != null) {
                    Text(
                        text = "Selected Patient",
                        style = MaterialTheme.typography.labelSmall,
                        color = MedicalBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = patient.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = "${patient.age} yrs • ${patient.phone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                } else {
                    Text(
                        text = "No Patient Selected",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Tap to choose a patient profile",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarningOrange
                    )
                }
            }

            // --- Navigation Hint ---
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Select Patient",
                tint = DividerColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}