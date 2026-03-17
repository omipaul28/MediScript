package com.nacoders.mediscript.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nacoders.mediscript.models.PrescriptionItem

@Composable
fun PrescriptionPreviewCard(medicines: List<PrescriptionItem>) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                "Prescription Preview",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            medicines.forEachIndexed { index, med ->

                Text("${index + 1}. ${med.name}")
                Text("Dosage: ${med.dosage}")
                Text("Duration: ${med.duration} ")
                Row() {
                    if(med.isMorning) Text(" Morning |")
                    if(med.isAfternoon) Text(" Afternoon |")
                    if(med.isNight) Text(" Night |")
                }

                if (med.notes.isNotBlank()) {
                    Text("Notes: ${med.notes}")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}