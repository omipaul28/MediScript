package com.nacoders.mediscript.viewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.dao.PrescriptionDao
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.data.local.repo.MedicineRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreatePrescriptionViewModel(
    private val repository: MedicineRepository,
    private val prescriptionDao: PrescriptionDao
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val suggestions: StateFlow<List<MedicineEntity>> =
        _query
            .debounce(300)
            .flatMapLatest { text ->
                if (text.isBlank()) {
                    flowOf(emptyList())
                } else {
                    repository.searchMedicines(text)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun updateQuery(text: String) {
        _query.value = text
    }
    fun savePrescription(prescription: PrescriptionEntity) {
        viewModelScope.launch {
            try {
                prescriptionDao.insertPrescription(prescription)
                println("DB_LOG: Prescription saved successfully for ${prescription.patientPhone}")
            } catch (e: Exception) {
                println("DB_LOG: Error saving prescription: ${e.message}")
            }
        }
    }
}