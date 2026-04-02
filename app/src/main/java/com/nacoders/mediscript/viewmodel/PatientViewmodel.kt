package com.nacoders.mediscript.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacoders.mediscript.data.local.dao.PatientDao
import com.nacoders.mediscript.data.local.dao.PrescriptionDao
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PatientViewModel(private val dao: PatientDao,
                       private val prescriptionDao: PrescriptionDao,
                       private val currentDoctorId: String
    ) : ViewModel() {
    private val _patient = MutableStateFlow<PatientEntity?>(null)
    val patient = _patient.asStateFlow()
    private val _prescription = MutableStateFlow<PrescriptionEntity?>(null)
    val prescription = _prescription.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val patients = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                dao.getPatientsByDoctor(currentDoctorId)
            } else {
                dao.searchPatients(currentDoctorId, query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun loadPatientByPhone(phone: String) {
        viewModelScope.launch {
            // Pass the doctorId to ensure we don't load another doctor's patient
            dao.getPatientByPhone(phone, currentDoctorId).collect {
                _patient.value = it
                it?.let { loadPrescriptionByPhone(it.phone) }
            }
        }
    }
    private fun loadPrescriptionByPhone(phone: String) {
        viewModelScope.launch {
            prescriptionDao.getLatestPrescriptionByPhone(phone, currentDoctorId).collect {
                _prescription.value = it
            }
        }
    }


    fun updatePatient(patient: PatientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updatePatient(patient)
        }
    }

    fun deletePatient(patient: PatientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deletePatient(patient)
        }
    }
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }


}