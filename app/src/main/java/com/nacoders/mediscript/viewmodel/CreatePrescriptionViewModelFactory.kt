package com.nacoders.mediscript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nacoders.mediscript.data.local.dao.PrescriptionDao
import com.nacoders.mediscript.data.local.repo.MedicineRepository

class CreatePrescriptionViewModelFactory(
    private val repository: MedicineRepository,
    private val prescriptionDao: PrescriptionDao // Add this
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatePrescriptionViewModel::class.java)) {
            // Pass the prescriptionDao to the ViewModel
            return CreatePrescriptionViewModel(repository, prescriptionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}