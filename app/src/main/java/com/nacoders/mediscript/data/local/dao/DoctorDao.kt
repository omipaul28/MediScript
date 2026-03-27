package com.nacoders.mediscript.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nacoders.mediscript.data.local.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: DoctorEntity)

    @Query("SELECT * FROM doctor_profile LIMIT 1")
    fun getDoctor(): Flow<DoctorEntity?>

    @Query("DELETE FROM doctor_profile")
    suspend fun clearDoctor()
}