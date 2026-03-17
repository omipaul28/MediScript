package com.nacoders.mediscript.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import com.nacoders.mediscript.data.local.repo.MedicineRepository
import kotlinx.coroutines.flow.*

class CreatePrescriptionViewModel(
    private val repository: MedicineRepository
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
}