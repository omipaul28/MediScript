package com.nacoders.mediscript.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import com.nacoders.mediscript.ui.theme.* // MedicalBlue, SurfaceWhite, etc.

@Composable
fun MedicineInputSection(
    medicineName: String,
    dosage: String,
    duration: String,
    isMorning: Boolean,
    isAfternoon: Boolean,
    isNight: Boolean,
    onMorningChange: (Boolean) -> Unit,
    onAfternoonChange: (Boolean) -> Unit,
    onNightChange: (Boolean) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    suggestions: List<MedicineEntity>,
    onMedicineChange: (String) -> Unit,
    onSuggestionClick: (MedicineEntity) -> Unit,
    onDosageChange: (String) -> Unit,
    onDurationChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // --- Medicine Search Field ---
        Column {
            OutlinedTextField(
                value = medicineName,
                onValueChange = {
                    onMedicineChange(it)
                    isFocused = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                        onFocusChanged(it.isFocused)
                    },
                leadingIcon = { Icon(Icons.Rounded.Search, null, tint = MedicalBlue) },
                shape = RoundedCornerShape(16.dp),
                label = { Text("Search Medicine (e.g. Paracetamol)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MedicalBlue,
                    unfocusedBorderColor = DividerColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            // --- Enhanced Suggestions List ---
            if (isFocused && suggestions.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .heightIn(max = 200.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = CardBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                    shadowElevation = 4.dp
                ) {
                    LazyColumn {
                        items(suggestions) { medicine ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSuggestionClick(medicine)
                                        isFocused = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Medication, null, tint = MedicalBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = medicine.name ?: "Unknown Medicine",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "${medicine.strength ?: ""} • ${medicine.form ?: ""}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                            }
                            HorizontalDivider(color = DividerColor, modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }

        // --- Dosage & Duration Row ---
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = dosage,
                onValueChange = onDosageChange,
                label = { Text("Dosage") },
                placeholder = { Text("1 tab") },
                leadingIcon = { Icon(Icons.Rounded.Science, null, tint = MedicalBlue, modifier = Modifier.size(20.dp)) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MedicalBlue, unfocusedBorderColor = DividerColor)
            )

            OutlinedTextField(
                value = duration,
                onValueChange = onDurationChange,
                label = { Text("Duration") },
                placeholder = { Text("5 days") },
                leadingIcon = { Icon(Icons.Rounded.Today, null, tint = MedicalBlue, modifier = Modifier.size(20.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MedicalBlue, unfocusedBorderColor = DividerColor)
            )
        }

        // --- Frequency Selector ---
        FrequencySelector(
            isMorning = isMorning,
            isAfternoon = isAfternoon,
            isNight = isNight,
            onMorningChange = onMorningChange,
            onAfternoonChange = onAfternoonChange,
            onNightChange = onNightChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencySelector(
    isMorning: Boolean,
    isAfternoon: Boolean,
    isNight: Boolean,
    onMorningChange: (Boolean) -> Unit,
    onAfternoonChange: (Boolean) -> Unit,
    onNightChange: (Boolean) -> Unit
) {
    Column {
        Text(
            "Timing / Frequency",
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FrequencyChip(label = "Morning", icon = Icons.Rounded.LightMode, selected = isMorning, onSelected = onMorningChange, modifier = Modifier.weight(1f))
            FrequencyChip(label = "Afternoon", icon = Icons.Rounded.WbTwilight, selected = isAfternoon, onSelected = onAfternoonChange, modifier = Modifier.weight(1f))
            FrequencyChip(label = "Night", icon = Icons.Rounded.NightsStay, selected = isNight, onSelected = onNightChange, modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier.height(44.dp),
        selected = selected,
        onClick = { onSelected(!selected) },
        label = { Text(label, fontSize = 12.sp) },
        leadingIcon = { Icon(icon, null, modifier = Modifier.size(16.dp)) },
        shape = RoundedCornerShape(12.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MedicalBlue,
            selectedLabelColor = Color.White,
            selectedLeadingIconColor = Color.White,
            containerColor = Color.Transparent
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = DividerColor,
            selectedBorderColor = MedicalBlue
        )
    )
}

@Composable
fun AddMedicineButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue)
    ) {
        Icon(Icons.Rounded.Add, null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Add to Prescription", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}