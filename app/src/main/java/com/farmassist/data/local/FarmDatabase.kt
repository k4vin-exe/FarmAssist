package com.farmassist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farmassist.data.local.dao.FarmDao
import com.farmassist.data.local.dao.NewsDao
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
        Scheme::class,
        NewsEntity::class
    ],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FarmDatabase : RoomDatabase() {

    abstract fun farmDao(): FarmDao
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: FarmDatabase? = null

        private fun seed(dao: FarmDao) {
            CoroutineScope(Dispatchers.IO).launch {
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

        fun getDatabase(context: Context): FarmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FarmDatabase::class.java,
                    "farm_assist_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { seed(it.farmDao()) }
                        }

                        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                            super.onDestructiveMigration(db)
                            INSTANCE?.let { seed(it.farmDao()) }
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dao = database.farmDao()
                                    if (dao.getAllSchemes().isEmpty()) {
                                        seed(dao)
                                    }
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
