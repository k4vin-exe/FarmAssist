package com.farmassist.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.farmassist.app.data.local.database.Converters

@Entity(tableName = "district_soil")
data class DistrictSoilEntity(
    @PrimaryKey val district: String,
    val soil: String
)

@Entity(tableName = "soil_data")
data class SoilDataEntity(
    @PrimaryKey val soil: String,
    val water: String,
    val fertility: String
)

@Entity(tableName = "crop_data")
@TypeConverters(Converters::class)
data class CropDataEntity(
    @PrimaryKey val crop: String,
    val soil: List<String>,
    val season: String,
    val temp_min: Int,
    val temp_max: Int,
    val growing_days: Int,
    val cost_per_acre: Int,
    val yield_per_acre: Int
)

@Entity(tableName = "crop_schedule")
data class CropScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val day: Int,
    val activity: String
)

@Entity(tableName = "fertilizer_data")
data class FertilizerDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val day: Int,
    val fertilizer: String
)

@Entity(tableName = "irrigation_data")
data class IrrigationDataEntity(
    @PrimaryKey val crop: String,
    val interval_days: Int
)

@Entity(tableName = "pest_data")
data class PestDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val condition: String,
    val risk: String
)

@Entity(tableName = "waste_management")
@TypeConverters(Converters::class)
data class WasteManagementEntity(
    @PrimaryKey val waste: String,
    val reuse: String,
    val steps: List<String>
)

@Entity(tableName = "terrace_farming")
data class TerraceFarmingEntity(
    @PrimaryKey val crop: String,
    val sunlight: String,
    val water: String,
    val days: Int
)

@Entity(tableName = "government_schemes")
data class GovtSchemeEntity(
    @PrimaryKey val name: String,
    val benefit: String,
    val eligibility: String
)
