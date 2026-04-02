package com.nacoders.mediscript.data.local.repo

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.nacoders.mediscript.data.local.AppDatabase
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.utils.loadMedicines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun pullDataFromFirestore(db: AppDatabase) {
    val firestore = FirebaseFirestore.getInstance()

    // 1. Listen for Patient changes
    firestore.collection("patients")
        .addSnapshotListener { snapshots, e ->
            if (e != null) return@addSnapshotListener

            snapshots?.let {
                val patients = it.toObjects(PatientEntity::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    // Use insertPatient (ensure OnConflictStrategy.REPLACE is set in DAO)
                    patients.forEach { patient ->
                        db.patientDao().insertPatient(patient.copy(isSynced = true))
                    }
                }
            }
        }

    // 2. Listen for Prescription changes
    firestore.collection("prescriptions")
        .addSnapshotListener { snapshots, e ->
            if (e != null) return@addSnapshotListener

            snapshots?.let {
                val prescriptions = it.toObjects(PrescriptionEntity::class.java)
                CoroutineScope(Dispatchers.IO).launch {
                    prescriptions.forEach { prescription ->
                        db.prescriptionDao().insertPrescription(prescription.copy(isSynced = true))
                    }
                }
            }
        }
}
// In a repository or a helper file
fun syncMedicinesFromFirebase(database: AppDatabase) {
    val firestore = FirebaseFirestore.getInstance()

    // Listen to the "medicines" collection
    firestore.collection("medicines")
        .addSnapshotListener { snapshots, e ->
            if (e != null) return@addSnapshotListener

            snapshots?.let {
                // Convert Firestore documents to MedicineEntity objects
                val remoteMedicines = it.toObjects(MedicineEntity::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    // Mark as synced so the app knows these are cloud-verified
                    val updatedMedicines = remoteMedicines.map { medicine ->
                        medicine.copy(isSynced = true)
                    }
                    // Save to Room for offline use
                    database.medicineDao().insertAll(updatedMedicines)
                }
            }
        }
}
fun uploadJsonToFirebase(context: Context) {
    val firestore = FirebaseFirestore.getInstance()
    val batch = firestore.batch() // Batching is faster for multiple entries

    // 1. Load the list using your existing utility
    val medicines = loadMedicines(context)

    medicines.forEach { medicine ->
        // 2. Use a unique ID (like the medicine name) to prevent duplicates
        val docRef = firestore.collection("medicines").document(medicine.name ?: "unknown")

        // 3. Mark as synced so the app knows this is a cloud-verified record
        batch.set(docRef, medicine.copy(isSynced = true))
    }

    // 4. Commit all changes at once
    batch.commit().addOnSuccessListener {
        println("SUCCESS: Firebase now contains your JSON medicine list!")
    }.addOnFailureListener { e ->
        println("ERROR: Failed to upload: ${e.message}")
    }
}