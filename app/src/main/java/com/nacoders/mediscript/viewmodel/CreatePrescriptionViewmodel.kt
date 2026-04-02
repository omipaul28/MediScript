package com.nacoders.mediscript.viewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.dao.PrescriptionDao
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.data.local.repo.MedicineRepository
import com.nacoders.mediscript.utils.SyncWorker
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
    // You'll need to pass Context to this function to use WorkManager
    fun savePrescription(context: Context, prescription: PrescriptionEntity) {
        viewModelScope.launch {
            try {
                prescriptionDao.insertPrescription(prescription)
                // Call the same scheduleSync logic here to upload the new prescription
                val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                    .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .build()
                WorkManager.getInstance(context).enqueueUniqueWork("data_sync", ExistingWorkPolicy.APPEND_OR_REPLACE, syncRequest)
            } catch (e: Exception) {
                // log error
            }
        }
    }
}