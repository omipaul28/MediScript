package com.nacoders.mediscript.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nacoders.mediscript.data.local.repo.MedicineRepository

class CreatePrescriptionViewModelFactory(
    private val repository: MedicineRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CreatePrescriptionViewModel::class.java)) {
            return CreatePrescriptionViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}