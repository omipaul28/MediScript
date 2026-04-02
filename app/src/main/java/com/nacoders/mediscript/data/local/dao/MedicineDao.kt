package com.nacoders.mediscript.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineEntity>)

    @Query("SELECT * FROM medicines")
    fun getMedicines(): Flow<List<MedicineEntity>>

    @Query("""
    SELECT * FROM medicines
    WHERE name LIKE '%' || :query || '%'
    ORDER BY name
    LIMIT 20
    """)
    fun autoComplete(query: String): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE isSynced = 0")
    suspend fun getUnsyncedMedicines(): List<MedicineEntity>
}


