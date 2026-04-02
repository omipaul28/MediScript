package com.nacoders.mediscript.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nacoders.mediscript.models.PrescriptionItem

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientPhone: String= "",
    val doctorId: String = "",
    val prescriptionNumber: String= "",
    val date: Long = System.currentTimeMillis(),
    val notes: String= "",
    val medicineList: List<PrescriptionItem> = emptyList(),
    val isSynced: Boolean = false
)