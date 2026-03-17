package com.nacoders.mediscript.models

data class PrescriptionItem(
    val name: String,
    val dosage: String,
    val duration: String,
    val isMorning: Boolean,
    val isAfternoon: Boolean,
    val isNight: Boolean,
    val notes: String
)
