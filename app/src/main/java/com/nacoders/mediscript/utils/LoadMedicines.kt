package com.nacoders.mediscript.utils

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.nacoders.mediscript.data.local.entity.MedicineEntity

fun loadMedicines(context: Context): List<MedicineEntity> {

    val json = context.assets
        .open("medicines.json")
        .bufferedReader()
        .use { it.readText() }

    val type = object : TypeToken<List<MedicineEntity>>() {}.type

    val medicines: List<MedicineEntity> = Gson().fromJson(json, type)

    return medicines.filter {
        !it.name.isNullOrBlank()
    }
}