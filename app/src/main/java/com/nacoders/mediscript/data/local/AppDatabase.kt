package com.nacoders.mediscript.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nacoders.mediscript.data.Converters
import com.nacoders.mediscript.data.local.dao.DoctorDao
import com.nacoders.mediscript.data.local.dao.MedicineDao
import com.nacoders.mediscript.data.local.dao.PatientDao
import com.nacoders.mediscript.data.local.dao.PrescriptionDao
import com.nacoders.mediscript.data.local.entity.DoctorEntity
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import com.nacoders.mediscript.utils.loadMedicines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [
        MedicineEntity::class,
        PatientEntity::class,
        PrescriptionEntity::class,
        DoctorEntity::class
               ],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
    abstract fun patientDao(): PatientDao
    abstract fun prescriptionDao(): PrescriptionDao
    abstract fun doctorDao(): DoctorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mediscript_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate only on first creation
                            INSTANCE?.let { database ->
                                prePopulateDatabase(context.applicationContext, database)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun prePopulateDatabase(context: Context, database: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val medicines = loadMedicines(context) //
                    if (medicines.isNotEmpty()) {
                        database.medicineDao().insertAll(medicines)
                    }
                } catch (e: Exception) {
                    // Log error in production using a tool like Crashlytics
                    e.printStackTrace()
                }
            }
        }
    }
}