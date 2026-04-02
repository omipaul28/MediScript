package com.nacoders.mediscript.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.ui.theme.CardBackground
import com.nacoders.mediscript.ui.theme.DividerColor
import com.nacoders.mediscript.ui.theme.MedicalBlue
import com.nacoders.mediscript.ui.theme.SurfaceWhite
import com.nacoders.mediscript.ui.theme.TextSecondary
import com.nacoders.mediscript.viewmodel.AddPatientViewmodel
import com.nacoders.mediscript.viewmodel.AddPatientViewmodelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val dao = db.patientDao()
    val currentDoctorId = remember {
        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }
    val viewModel: AddPatientViewmodel = viewModel(
        factory = AddPatientViewmodelFactory(dao)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add New Patient",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SurfaceWhite)
            )
        },
        containerColor = SurfaceWhite
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- Form Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Patient Identification",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    // Name Field
                    PatientInputField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Patient Full Name",
                        icon = Icons.Rounded.Person
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Age Field
                        PatientInputField(
                            value = age,
                            onValueChange = { age = it },
                            label = "Age",
                            icon = Icons.Rounded.Cake,
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number
                        )
                        // Gender Field
                        PatientInputField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = "Gender",
                            icon = Icons.Rounded.Wc,
                            modifier = Modifier.weight(1.2f)
                        )
                    }

                    // Phone Field
                    PatientInputField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Contact Number",
                        icon = Icons.Rounded.Phone,
                        keyboardType = KeyboardType.Phone
                    )

                    // Address Field
                    PatientInputField(
                        value = address,
                        onValueChange = { address = it },
                        label = "Residential Address",
                        icon = Icons.Rounded.LocationOn,
                        singleLine = false,
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Save Button
                    Button(
                        onClick = {
                            if (name.isBlank() || age.isBlank() || phone.isBlank()) return@Button

                            val patient = PatientEntity(
                                doctorId = currentDoctorId,
                                name = name,
                                age = age,
                                gender = gender,
                                phone = phone,
                                address = address
                            )
                            viewModel.savePatient(context, patient)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue)
                    ) {
                        Icon(Icons.Rounded.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Register Patient",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Reusable input field for the Patient Registration form
 */
@Composable
fun PatientInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = { Icon(icon, contentDescription = null, tint = MedicalBlue, modifier = Modifier.size(20.dp)) },
        shape = RoundedCornerShape(16.dp),
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MedicalBlue,
            unfocusedBorderColor = DividerColor,
            focusedLabelColor = MedicalBlue,
            // Replace containerColor with these two:
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}