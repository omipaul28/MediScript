package com.nacoders.mediscript.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun MedicineInputSection(
    medicineViewModel: MedicineViewModel,
    medicineName: String,
    dosage: String,
    duration: String,
    notes: String,
    onMedicineChange: (String) -> Unit,
    onDosageChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onNotesChange: (String) -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            "Medicine",
            style = MaterialTheme.typography.titleMedium
        )

        MedicineAutocompleteField(
            viewModel = medicineViewModel,
            selectedMedicine = medicineName,
            onMedicineSelected = onMedicineChange
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            OutlinedTextField(
                value = dosage,
                onValueChange = onDosageChange,
                label = { Text("Dosage") },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value = duration,
                onValueChange = onDurationChange,
                label = { Text("Duration (days)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium
            )
        }

        FrequencySelector()

        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            minLines = 2
        )

        VoiceInputButton()
    }
}


@Composable
fun FrequencySelector() {

    var selected by remember { mutableStateOf(setOf<String>()) }

    val options = listOf("Morning", "Afternoon", "Night")

    Column {

        Text(
            "Frequency",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            options.forEach { option ->

                FilterChip(
                    selected = selected.contains(option),

                    onClick = {
                        selected =
                            if (selected.contains(option))
                                selected - option
                            else
                                selected + option
                    },

                    label = { Text(option) }
                )
            }
        }
    }
}

@Composable
fun VoiceInputButton() {

    OutlinedButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth()
    ) {

        Icon(Icons.Default.Phone, null)

        Spacer(modifier = Modifier.width(8.dp))

        Text("Voice Input")
    }
}

@Composable
fun AddMedicineButton() {

    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        Icon(Icons.Default.Add, null)

        Spacer(modifier = Modifier.width(8.dp))

        Text("Add Medicine")
    }
}

@Composable
fun PrescriptionPreviewCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                "Prescription Preview",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("1. Napa 500mg")
            Text("Dosage: 1 tablet")
            Text("Morning + Night")
            Text("Duration: 5 days")
        }
    }
}