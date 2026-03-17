package com.nacoders.mediscript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacoders.mediscript.data.local.dao.PatientDao
import com.nacoders.mediscript.data.local.entity.PatientEntity
import kotlinx.coroutines.launch

class AddPatientViewmodel(private val dao: PatientDao) : ViewModel() {
    fun savePatient(patient: PatientEntity) {
        viewModelScope.launch {
            dao.insertPatient(patient)
        }
    }
}