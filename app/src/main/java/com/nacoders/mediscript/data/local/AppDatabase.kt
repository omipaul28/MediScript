package com.nacoders.mediscript.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nacoders.mediscript.data.local.dao.MedicineDao
import com.nacoders.mediscript.data.local.entity.MedicineEntity
import com.nacoders.mediscript.utils.loadMedicines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MedicineEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Trigger population once the DB is created for the first time
                            INSTANCE?.let { database ->
                                prePopulateDatabase(context, database)
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
                    val medicines = loadMedicines(context)
                    if (medicines.isNotEmpty()) {
                        database.medicineDao().insertAll(medicines)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}