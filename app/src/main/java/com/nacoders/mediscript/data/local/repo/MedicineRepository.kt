package com.nacoders.mediscript.data.local.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.nacoders.mediscript.data.local.dao.MedicineDao
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MedicineRepository(
    private val dao: MedicineDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    // UI always observes local Room data
    fun searchMedicines(query: String): Flow<List<MedicineEntity>> {
        return dao.autoComplete(query)
    }
}