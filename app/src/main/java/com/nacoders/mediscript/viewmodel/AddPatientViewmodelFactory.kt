package com.nacoders.mediscript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nacoders.mediscript.data.local.dao.PatientDao

class AddPatientViewmodelFactory(
    private val patientDao: PatientDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AddPatientViewmodel::class.java)) {
            return AddPatientViewmodel(patientDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}