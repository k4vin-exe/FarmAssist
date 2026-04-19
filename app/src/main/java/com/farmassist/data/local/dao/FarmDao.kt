package com.farmassist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farmassist.data.local.model.*

@Dao
interface FarmDao {

    // DistrictSoil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistrictSoils(items: List<DistrictSoil>)
    
    @Query("SELECT * FROM district_soil WHERE district = :district")
    suspend fun getDistrictInfo(district: String): DistrictSoil?

    @Query("SELECT * FROM district_soil ORDER BY district ASC")
    fun getAllDistricts(): kotlinx.coroutines.flow.Flow<List<DistrictSoil>>

    // Soil
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSoils(items: List<Soil>)
    @Query("SELECT * FROM soil WHERE soil = :soil")
    suspend fun getSoilDetails(soil: String): Soil?

    // Crop
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrops(items: List<Crop>)
    @Query("SELECT * FROM crop")
    suspend fun getAllCrops(): List<Crop>
    
    // Get crop suggestions perfectly filtered by temp range, acceptable seasons, and JSON-converted soil types
    @Query("SELECT * FROM crop WHERE temp_min <= :temp AND temp_max >= :temp AND season IN (:season, 'All') AND soil LIKE '%' || :soilType || '%'")
    suspend fun getRecommendedCrops(soilType: String, season: String, temp: Int): List<Crop>
    
    // CropSchedule
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropSchedules(items: List<CropSchedule>)
    @Query("SELECT * FROM crop_schedule WHERE crop = :crop ORDER BY day ASC")
    suspend fun getScheduleForCrop(crop: String): List<CropSchedule>

    // Fertilizer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFertilizers(items: List<Fertilizer>)
    @Query("SELECT * FROM fertilizer WHERE crop = :crop ORDER BY day ASC")
    suspend fun getFertilizersForCrop(crop: String): List<Fertilizer>

    // Irrigation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIrrigation(items: List<Irrigation>)
    @Query("SELECT * FROM irrigation WHERE crop = :crop")
    suspend fun getIrrigationForCrop(crop: String): Irrigation?

    // Pest
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPests(items: List<Pest>)
    @Query("SELECT * FROM pest WHERE crop = :crop")
    suspend fun getPestsForCrop(crop: String): List<Pest>

    // Waste
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWastes(items: List<Waste>)
    @Query("SELECT * FROM waste")
    suspend fun getAllWastes(): List<Waste>

    // Terrace Farming
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerraceFarming(items: List<TerraceFarming>)
    @Query("SELECT * FROM terrace_farming")
    suspend fun getAllTerraceFarming(): List<TerraceFarming>

    // Schemes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchemes(items: List<Scheme>)
    @Query("SELECT * FROM scheme")
    suspend fun getAllSchemes(): List<Scheme>
    
    @Query("DELETE FROM scheme")
    suspend fun clearSchemes()
}
