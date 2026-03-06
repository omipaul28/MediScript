package com.nacoders.mediscript.models

data class MedicineJson(

    val name: String,
    val strength: String,
    val form: String,
    val defaultDosage: String,
    val defaultFrequency: String
)