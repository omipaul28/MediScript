package com.nacoders.mediscript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nacoders.mediscript.data.local.dao.PatientDao
import com.nacoders.mediscript.data.local.dao.PrescriptionDao

class PatientViewmodelFactory(
    private val dao: PatientDao,
    private val prescriptionDao: PrescriptionDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientViewModel::class.java)) {
            return PatientViewModel(dao, prescriptionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}