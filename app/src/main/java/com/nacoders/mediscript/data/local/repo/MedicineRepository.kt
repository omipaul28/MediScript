package com.nacoders.mediscript.data.local.repo

import com.nacoders.mediscript.data.local.dao.MedicineDao
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import kotlinx.coroutines.flow.Flow

class MedicineRepository(
    private val dao: MedicineDao
) {

    fun searchMedicines(query: String): Flow<List<MedicineEntity>> {
        return dao.autoComplete(query)

    }
}