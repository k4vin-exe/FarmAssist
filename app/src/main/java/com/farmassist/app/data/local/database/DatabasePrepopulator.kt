package com.farmassist.app.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farmassist.app.data.local.entity.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.InputStreamReader

class DatabasePrepopulator(private val context: Context) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            try {
                prePopulateDatabase()
            } catch (e: Exception) {
                Log.e("DB_PREPOPULATOR", "Error pre-populating database", e)
            }
        }
    }

    private suspend fun prePopulateDatabase() {
        val database = FarmAssistDatabase.getDatabase(context)
        val dao = database.farmAssistDao()

        context.assets.open("farm_data.json").use { inputStream ->
            val reader = InputStreamReader(inputStream)
            val dataWrapper = Gson().fromJson(reader, FarmDataWrapper::class.java)

            dataWrapper.district_soil?.let { dao.insertDistrictSoils(it) }
            dataWrapper.soil_data?.let { dao.insertSoilData(it) }
            dataWrapper.crop_data?.let { dao.insertCropData(it) }
            dataWrapper.crop_schedule?.let { dao.insertCropSchedules(it) }
            dataWrapper.fertilizer_data?.let { dao.insertFertilizerData(it) }
            dataWrapper.irrigation_data?.let { dao.insertIrrigationData(it) }
            dataWrapper.pest_data?.let { dao.insertPestData(it) }
            dataWrapper.waste_management?.let { dao.insertWasteManagementData(it) }
            dataWrapper.terrace_farming?.let { dao.insertTerraceFarmingData(it) }
            dataWrapper.government_schemes?.let { dao.insertGovtSchemes(it) }
        }
    }
}

data class FarmDataWrapper(
    val district_soil: List<DistrictSoilEntity>?,
    val soil_data: List<SoilDataEntity>?,
    val crop_data: List<CropDataEntity>?,
    val crop_schedule: List<CropScheduleEntity>?,
    val fertilizer_data: List<FertilizerDataEntity>?,
    val irrigation_data: List<IrrigationDataEntity>?,
    val pest_data: List<PestDataEntity>?,
    val waste_management: List<WasteManagementEntity>?,
    val terrace_farming: List<TerraceFarmingEntity>?,
    val government_schemes: List<GovtSchemeEntity>?
)
