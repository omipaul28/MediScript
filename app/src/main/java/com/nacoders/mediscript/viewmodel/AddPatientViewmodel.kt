package com.nacoders.mediscript.viewmodel

import android.content.Context
import androidx.work.Constraints
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.nacoders.mediscript.data.local.dao.PatientDao
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.utils.SyncWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AddPatientViewmodel(private val dao: PatientDao) : ViewModel() {
    fun savePatient(context: Context, patient: PatientEntity) { // Added context parameter
        viewModelScope.launch {
            dao.insertPatient(patient)
            scheduleSync(context) // Trigger the sync here!
        }
    }
    fun scheduleSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when online
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "data_sync",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            syncRequest
        )
    }
}