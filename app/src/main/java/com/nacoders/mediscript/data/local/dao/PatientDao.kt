package com.nacoders.mediscript.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nacoders.mediscript.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {


    @Query("SELECT * FROM patients ORDER BY id DESC")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE doctorId = :doctorId ORDER BY id DESC")
    fun getPatientsByDoctor(doctorId: String): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE phone = :phone AND doctorId = :doctorId LIMIT 1")
    fun getPatientByPhone(phone: String, doctorId: String): Flow<PatientEntity?>

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)

    @Query("SELECT * FROM patients WHERE isSynced = 0")
    suspend fun getUnsyncedPatients(): List<PatientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Query("""
    SELECT * FROM patients 
    WHERE doctorId = :doctorId 
    AND (name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%')
    ORDER BY name ASC
""")
    fun searchPatients(doctorId: String, query: String): Flow<List<PatientEntity>>
}

