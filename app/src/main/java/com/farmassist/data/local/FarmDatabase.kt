package com.farmassist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farmassist.data.local.dao.FarmDao
import com.farmassist.data.local.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        DistrictSoil::class,
        Soil::class,
        Crop::class,
        CropSchedule::class,
        Fertilizer::class,
        Irrigation::class,
        Pest::class,
        Waste::class,
        TerraceFarming::class,
        Scheme::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FarmDatabase : RoomDatabase() {

    abstract fun farmDao(): FarmDao

    companion object {
        @Volatile
        private var INSTANCE: FarmDatabase? = null

        fun getDatabase(context: Context): FarmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FarmDatabase::class.java,
                    "farm_assist_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(FarmDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class FarmDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = database.farmDao()
                    
                    dao.insertDistrictSoils(SeedData.districtSoils)
                    dao.insertSoils(SeedData.soils)
                    dao.insertCrops(SeedData.crops)
                    dao.insertCropSchedules(SeedData.cropSchedules)
                    dao.insertFertilizers(SeedData.fertilizers)
                    dao.insertIrrigation(SeedData.irrigations)
                    dao.insertPests(SeedData.pests)
                    dao.insertWastes(SeedData.wastes)
                    dao.insertTerraceFarming(SeedData.terraceFarmingList)
                    dao.insertSchemes(SeedData.schemes)
                }
            }
        }
    }
}
