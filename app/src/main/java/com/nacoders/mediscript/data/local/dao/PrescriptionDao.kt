package com.nacoders.mediscript.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {

    @Insert
    suspend fun insertPrescription(prescription: PrescriptionEntity)

    // Fetches all prescriptions for a specific patient (One patient -> Multiple prescriptions)
    @Query("SELECT * FROM prescriptions WHERE patientPhone = :phone ORDER BY date DESC")
    fun getPrescriptionsByPatient(phone: String): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions ORDER BY date DESC")
    fun getAllPrescriptions(): Flow<List<PrescriptionEntity>>

    @Query("SELECT * FROM prescriptions WHERE patientPhone = :phone ORDER BY date DESC LIMIT 1")
    fun getLatestPrescriptionByPhone(phone: String): Flow<PrescriptionEntity?>
}