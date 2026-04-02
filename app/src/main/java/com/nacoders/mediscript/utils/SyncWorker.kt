package com.nacoders.mediscript.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.nacoders.mediscript.data.local.AppDatabase
import kotlinx.coroutines.tasks.await

class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getInstance(applicationContext)
        val firestore = FirebaseFirestore.getInstance()

        return try {
            // 1. Sync Patients
            val unsyncedPatients = db.patientDao().getUnsyncedPatients()
            unsyncedPatients.forEach { patient ->
                firestore.collection("patients").document(patient.phone).set(patient).await()
                db.patientDao().updatePatient(patient.copy(isSynced = true))
            }

            // 2. Sync Prescriptions
            val unsyncedPrescriptions = db.prescriptionDao().getUnsyncedPrescriptions()
            unsyncedPrescriptions.forEach { prescription ->
                // Use a unique ID for the document, or let Firestore generate one
                firestore.collection("prescriptions").document().set(prescription).await()

                // FIX: Update local Room record to isSynced = true
                db.prescriptionDao().updatePrescription(prescription.copy(isSynced = true))
            }
            // 3. Sync Custom Medicines
            val unsyncedMedicines = db.medicineDao().getUnsyncedMedicines()
            unsyncedMedicines.forEach { medicine ->
                firestore.collection("medicines").document().set(medicine).await()
                db.medicineDao().insertAll(listOf(medicine.copy(isSynced = true)))
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry() // WorkManager will handle the backoff policy
        }
    }
}