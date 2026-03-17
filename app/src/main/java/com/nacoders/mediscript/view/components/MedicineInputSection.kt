package com.nacoders.mediscript.view.components

import android.R.attr.text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nacoders.mediscript.data.local.entity.MedicineEntity


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
    notes: String,
    onFocusChanged: (Boolean) -> Unit,
    suggestions: List<MedicineEntity>,
    onMedicineChange: (String) -> Unit,
    onSuggestionClick: (MedicineEntity) -> Unit,
    onDosageChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onNotesChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }


    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        OutlinedTextField(
            value = medicineName,
            onValueChange = onMedicineChange,
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged{
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
                },
            shape = MaterialTheme.shapes.medium,
            label = { Text("Search medicine") }
        )

        if (isFocused && suggestions.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {

                items(suggestions) { medicine ->

                    Text(
                        text = "${medicine.name} (${medicine.strength})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSuggestionClick(medicine)
                                isFocused = false
                            }
                            .padding(12.dp)
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            OutlinedTextField(
                value = dosage,
                onValueChange = onDosageChange,
                label = { Text("Dosage") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = duration,
                onValueChange = onDurationChange,
                label = { Text("Duration (days)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f)
            )
        }

        FrequencySelector(
            isMorning = isMorning,
            isAfternoon = isAfternoon,
            isNight = isNight,
            onMorningChange = onMorningChange,
            onAfternoonChange = onAfternoonChange,
            onNightChange = onNightChange
        )

        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        VoiceInputButton()
    }
}


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
            "Frequency",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            FilterChip(
                selected = isMorning,
                onClick = { onMorningChange(!isMorning) },
                label = { Text("Morning") }
            )

            FilterChip(
                selected = isAfternoon,
                onClick = { onAfternoonChange(!isAfternoon) },
                label = { Text("Afternoon") }
            )

            FilterChip(
                selected = isNight,
                onClick = { onNightChange(!isNight) },
                label = { Text("Night") }
            )
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
fun AddMedicineButton(onClick: () -> Unit) {

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        Icon(Icons.Default.Add, null)

        Spacer(modifier = Modifier.width(8.dp))

        Text("Add Medicine")
    }
}

