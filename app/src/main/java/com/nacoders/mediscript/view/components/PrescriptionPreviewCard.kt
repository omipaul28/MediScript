package com.nacoders.mediscript.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nacoders.mediscript.models.PrescriptionItem
import com.nacoders.mediscript.ui.theme.* // Using MedicalBlue, CardBackground, TextSecondary, etc.

@Composable
fun PrescriptionPreviewCard(medicines: List<PrescriptionItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // --- Header Section ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.AssignmentTurnedIn,
                    contentDescription = null,
                    tint = MedicalBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Prescription Preview",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (medicines.isEmpty()) {
                // Empty state to guide the doctor
                Text(
                    text = "No medications added yet. Use the form above to build the prescription.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                medicines.forEachIndexed { index, med ->
                    PreviewMedicineItem(index + 1, med)

                    // Add a subtle divider between items, but not after the last one
                    if (index < medicines.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = DividerColor,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PreviewMedicineItem(index: Int, med: PrescriptionItem) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MedicalBlue,
                modifier = Modifier.width(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                // Medicine Name
                Text(
                    text = med.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dosage and Duration Row
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DetailLabel(label = "Dosage", value = med.dosage)
                    DetailLabel(label = "Duration", value = med.duration)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Frequency Badges
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (med.isMorning) FrequencyBadge("Morning")
                    if (med.isAfternoon) FrequencyBadge("Afternoon")
                    if (med.isNight) FrequencyBadge("Night")

                    if (!med.isMorning && !med.isAfternoon && !med.isNight) {
                        FrequencyBadge("As Needed", isSpecial = true)
                    }
                }

                if (med.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Note: ${med.notes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun DetailLabel(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = TextPrimary
        )
    }
}

@Composable
fun FrequencyBadge(text: String, isSpecial: Boolean = false) {
    Surface(
        color = if (isSpecial) WarningOrange.copy(alpha = 0.1f) else MedicalBlueLight.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSpecial) WarningOrange else MedicalBlueDark,
            fontWeight = FontWeight.Bold
        )
    }
}