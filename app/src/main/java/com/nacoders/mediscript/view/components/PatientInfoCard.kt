package com.nacoders.mediscript.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.navigation.NavRoutes

@Composable
fun PatientInfoCard(patient: PatientEntity?, navController: NavController) {

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = {
            navController.navigate(NavRoutes.PATIENT_LIST)
        }
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            if (patient != null) {
                Column {

                    Text(
                        text = patient.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = patient.age,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = patient.phone,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }else{
                Text(
                    text = "No Patient Selected",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Tap to select",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}