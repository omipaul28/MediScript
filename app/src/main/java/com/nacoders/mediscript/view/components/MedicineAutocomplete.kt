package com.nacoders.mediscript.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun MedicineAutocompleteField(
    viewModel: MedicineViewModel,
    selectedMedicine: String,
    onMedicineSelected: (String) -> Unit
) {

    val suggestions by viewModel.suggestions.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column {

        OutlinedTextField(
            value = selectedMedicine,

            onValueChange = {

                onMedicineSelected(it)
                viewModel.searchMedicine(it)
                expanded = true
            },

            label = { Text("Search Medicine") },

            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded && suggestions.isNotEmpty(),

            onDismissRequest = {
                expanded = false
            }
        ) {

            suggestions.forEach { medicine ->

                DropdownMenuItem(

                    text = {
                        Text(medicine.name)
                    },

                    onClick = {

                        onMedicineSelected(medicine.name)

                        expanded = false
                    }
                )
            }
        }
    }
}