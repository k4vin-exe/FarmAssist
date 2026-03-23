package com.farmassist.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.farmassist.app.data.local.dao.FarmAssistDao
import com.farmassist.app.data.local.entity.*

@Database(
    entities = [
        DistrictSoilEntity::class, 
        SoilDataEntity::class, 
        CropDataEntity::class, 
        CropScheduleEntity::class, 
        FertilizerDataEntity::class, 
        IrrigationDataEntity::class, 
        PestDataEntity::class, 
        WasteManagementEntity::class, 
        TerraceFarmingEntity::class, 
        GovtSchemeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FarmAssistDatabase : RoomDatabase() {

    abstract fun farmAssistDao(): FarmAssistDao

    companion object {
        @Volatile
        private var INSTANCE: FarmAssistDatabase? = null

        fun getDatabase(context: Context): FarmAssistDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FarmAssistDatabase::class.java,
                    "farm_assist_database"
                )
                .addCallback(DatabasePrepopulator(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
