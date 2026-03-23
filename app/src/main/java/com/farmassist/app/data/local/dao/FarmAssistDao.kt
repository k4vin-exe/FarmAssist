package com.farmassist.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmassist.app.data.local.entity.*

@Dao
interface FarmAssistDao {

    @Query("SELECT soil FROM district_soil WHERE district = :district LIMIT 1")
    suspend fun getSoilByDistrict(district: String): String?

    @Query("""
        SELECT * FROM crop_data 
        WHERE (soil LIKE '%' || :userSoil || '%') 
        AND (season = :currentSeason OR season = 'All') 
        AND (:currentTemp BETWEEN temp_min AND temp_max)
    """)
    suspend fun getRecommendedCrops(userSoil: String, currentSeason: String, currentTemp: Int): List<CropDataEntity>

    @Query("SELECT * FROM crop_schedule WHERE crop = :cropName ORDER BY day ASC")
    suspend fun getScheduleForCrop(cropName: String): List<CropScheduleEntity>

    @Query("SELECT * FROM fertilizer_data WHERE crop = :cropName ORDER BY day ASC")
    suspend fun getFertilizerForCrop(cropName: String): List<FertilizerDataEntity>
    
    @Query("SELECT * FROM irrigation_data WHERE crop = :cropName LIMIT 1")
    suspend fun getIrrigationForCrop(cropName: String): IrrigationDataEntity?

    @Query("SELECT * FROM terrace_farming")
    suspend fun getAllTerraceFarmingData(): List<TerraceFarmingEntity>

    @Query("SELECT * FROM waste_management")
    suspend fun getAllWasteManagementData(): List<WasteManagementEntity>

    @Query("SELECT * FROM government_schemes")
    suspend fun getAllGovtSchemes(): List<GovtSchemeEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistrictSoils(items: List<DistrictSoilEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoilData(items: List<SoilDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropData(items: List<CropDataEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropSchedules(items: List<CropScheduleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFertilizerData(items: List<FertilizerDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIrrigationData(items: List<IrrigationDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPestData(items: List<PestDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWasteManagementData(items: List<WasteManagementEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerraceFarmingData(items: List<TerraceFarmingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGovtSchemes(items: List<GovtSchemeEntity>)
}
