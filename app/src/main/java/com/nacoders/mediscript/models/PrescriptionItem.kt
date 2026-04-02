package com.nacoders.mediscript.models

data class PrescriptionItem(
    val name: String= "",
    val dosage: String= "",
    val duration: String= "",
    val isMorning: Boolean= false,
    val isAfternoon: Boolean = false,
    val isNight: Boolean= false,
    val notes: String= ""
)
