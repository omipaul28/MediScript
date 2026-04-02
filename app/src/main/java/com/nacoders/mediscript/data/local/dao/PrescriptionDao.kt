package com.nacoders.mediscript.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionEntity)

    // Fetches all prescriptions for a specific patient (One patient -> Multiple prescriptions)
    @Query("SELECT * FROM prescriptions WHERE patientPhone = :phone ORDER BY date DESC")
    fun getPrescriptionsByPatient(phone: String): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions ORDER BY date DESC")
    fun getAllPrescriptions(): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions WHERE patientPhone = :phone AND doctorId = :doctorId ORDER BY date DESC LIMIT 1")
    fun getLatestPrescriptionByPhone(phone: String, doctorId: String): Flow<PrescriptionEntity?>

    @Query("SELECT * FROM prescriptions WHERE isSynced = 0")
    suspend fun getUnsyncedPrescriptions(): List<PrescriptionEntity>

    @Update
    suspend fun updatePrescription(prescription: PrescriptionEntity)
}