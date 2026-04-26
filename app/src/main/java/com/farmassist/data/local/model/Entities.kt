package com.farmassist.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.farmassist.data.local.Converters

@Entity(tableName = "district_soil")
data class DistrictSoil(
    @PrimaryKey val district: String,
    val soil: String,
    val defaultTemp: Int,
    val lat: Double,
    val lng: Double
)

@Entity(tableName = "soil")
data class Soil(
    @PrimaryKey val soil: String,
    val water: String,
    val fertility: String
)

@Entity(tableName = "crop")
@TypeConverters(Converters::class)
data class Crop(
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
data class CropSchedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val day: Int,
    val stage: String = "",
    val activity: String,
    val description: String = ""
)

@Entity(tableName = "fertilizer")
data class Fertilizer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val day: Int,
    val fertilizer: String,
    val description: String = ""
)

@Entity(tableName = "irrigation")
data class Irrigation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val interval_days: Int
)

@Entity(tableName = "pest")
data class Pest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val condition: String,
    val risk: String
)

@Entity(tableName = "waste")
@TypeConverters(Converters::class)
data class Waste(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val waste: String,
    val reuse: String,
    val steps: List<String>
)

@Entity(tableName = "terrace_farming")
@TypeConverters(Converters::class)
data class TerraceFarming(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val crop: String,
    val sunlight: String,         // Full / Partial / Shade
    val water: String,            // Daily / Weekly / Alternate days
    val days: Int,                // days to harvest
    val difficulty: String = "Easy",        // Easy / Medium / Hard
    val containerSize: String = "Medium",   // Small / Medium / Large / Any
    val emoji: String = "🌱",
    val description: String = "",
    val tips: List<String> = emptyList()
)

@Entity(tableName = "scheme")
data class Scheme(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val benefit: String,
    val eligibility: String
)

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val guid: String,
    val title: String,
    val pubDate: String,
    val link: String,
    val description: String,
    val tag: String
)
