package com.nacoders.mediscript.data

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.nacoders.mediscript.models.PrescriptionItem

class Converters {
    @TypeConverter
    fun fromMedicineList(value: List<PrescriptionItem>): String = Gson().toJson(value)

    @TypeConverter
    fun toMedicineList(value: String): List<PrescriptionItem> =
        Gson().fromJson(value, object : TypeToken<List<PrescriptionItem>>() {}.type)
}