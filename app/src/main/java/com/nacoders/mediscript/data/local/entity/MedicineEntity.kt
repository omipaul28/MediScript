package com.nacoders.mediscript.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicines",
    indices = [Index(value = ["name"])]
)
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Default to 0 so Room handles generation

    val name: String?,
    val strength: String?,
    val form: String?
)