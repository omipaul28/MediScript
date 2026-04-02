package com.nacoders.mediscript.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val doctorId: String = "",
    val name: String = "",
    val age: String= "",
    val gender: String= "",
    val phone: String= "",
    val address: String= "",
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)