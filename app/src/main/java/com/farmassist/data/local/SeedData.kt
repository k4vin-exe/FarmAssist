package com.farmassist.data.local

import com.farmassist.data.local.model.*

object SeedData {

    val districtSoils = listOf(
        DistrictSoil("Ariyalur", "Red", 29, 11.1400, 79.0786),
        DistrictSoil("Chengalpattu", "Red", 29, 12.6840, 79.9757),
        DistrictSoil("Chennai", "Sandy", 29, 13.0827, 80.2707),
        DistrictSoil("Coimbatore", "Red", 28, 11.0168, 76.9558),
        DistrictSoil("Cuddalore", "Alluvial", 29, 11.7480, 79.7714),
        DistrictSoil("Dharmapuri", "Red", 28, 12.1211, 78.1582),
        DistrictSoil("Dindigul", "Red", 28, 10.3673, 77.9803),
        DistrictSoil("Erode", "Black", 32, 11.3410, 77.7172),
        DistrictSoil("Kallakurichi", "Black", 29, 11.7380, 78.9620),
        DistrictSoil("Kanchipuram", "Red", 29, 12.8342, 79.7036),
        DistrictSoil("Kanyakumari", "Laterite", 27, 8.0883, 77.5385),
        DistrictSoil("Karur", "Red", 29, 10.9601, 78.0766),
        DistrictSoil("Krishnagiri", "Red", 27, 12.5186, 78.2137),
        DistrictSoil("Madurai", "Red", 29, 9.9252, 78.1198),
        DistrictSoil("Mayiladuthurai", "Alluvial", 29, 11.1026, 79.6542),
        DistrictSoil("Nagapattinam", "Alluvial", 29, 10.7672, 79.8449),
        DistrictSoil("Namakkal", "Red", 28, 11.2189, 78.1674),
        DistrictSoil("Perambalur", "Black", 29, 11.2333, 78.8833),
        DistrictSoil("Pudukkottai", "Red", 29, 10.3797, 78.8208),
        DistrictSoil("Ramanathapuram", "Sandy", 30, 9.3639, 78.8306),
        DistrictSoil("Ranipet", "Red", 29, 12.9272, 79.3332),
        DistrictSoil("Salem", "Red", 28, 11.6643, 78.1460),
        DistrictSoil("Sivaganga", "Red", 29, 9.8433, 78.4809),
        DistrictSoil("Tenkasi", "Red", 28, 8.9594, 77.3142),
        DistrictSoil("Thanjavur", "Alluvial", 29, 10.7870, 79.1378),
        DistrictSoil("The Nilgiris", "Laterite", 18, 11.4916, 76.7337),
        DistrictSoil("Theni", "Red", 28, 10.0104, 77.4768),
        DistrictSoil("Thoothukudi", "Black", 30, 8.7642, 78.1348),
        DistrictSoil("Tiruchirappalli", "Red", 29, 10.7905, 78.7047),
        DistrictSoil("Tirunelveli", "Red", 29, 8.7139, 77.7567),
        DistrictSoil("Tirupathur", "Red", 28, 12.4938, 78.5638),
        DistrictSoil("Tiruppur", "Red", 28, 11.1085, 77.3411),
        DistrictSoil("Tiruvallur", "Red", 29, 13.1436, 79.9125),
        DistrictSoil("Tiruvannamalai", "Red", 29, 12.2253, 79.0747),
        DistrictSoil("Tiruvarur", "Alluvial", 29, 10.7661, 79.6344),
        DistrictSoil("Vellore", "Red", 29, 12.9165, 79.1325),
        DistrictSoil("Viluppuram", "Red", 29, 11.9401, 79.4861),
        DistrictSoil("Virudhunagar", "Black", 30, 9.5872, 77.9514)
    )

    val soils = listOf(
        Soil("Red", "Medium", "Medium"),
        Soil("Black", "High", "High"),
        Soil("Alluvial", "High", "Very High"),
        Soil("Sandy", "Low", "Low")
    )

    val crops = listOf(
        Crop("Rice", listOf("Alluvial", "Clay"), "Rainy", 20, 35, 120, 25000, 2500),
        Crop("Groundnut", listOf("Red", "Sandy"), "Summer", 25, 35, 110, 18000, 900),
        Crop("Cotton", listOf("Black"), "Summer", 25, 40, 150, 30000, 1200),
        Crop("Maize", listOf("Red"), "All", 18, 32, 100, 20000, 2000),
        Crop("Ragi", listOf("Red"), "Rainy", 20, 30, 90, 15000, 1200),
        Crop("Sugarcane", listOf("Alluvial"), "All", 20, 38, 300, 40000, 35000),
        Crop("Banana", listOf("Alluvial"), "All", 22, 35, 270, 50000, 30000),
        Crop("Coconut", listOf("Sandy"), "All", 20, 35, 365, 20000, 10000)
    )

    val cropSchedules = listOf(
        CropSchedule(0, "Rice", 1, "Land Preparation"),
        CropSchedule(0, "Rice", 7, "Sowing"),
        CropSchedule(0, "Rice", 20, "Fertilizer"),
        CropSchedule(0, "Rice", 30, "Irrigation"),
        CropSchedule(0, "Rice", 60, "Pest Monitoring"),
        CropSchedule(0, "Rice", 120, "Harvest"),
        CropSchedule(0, "Groundnut", 1, "Land Preparation"),
        CropSchedule(0, "Groundnut", 10, "Sowing"),
        CropSchedule(0, "Groundnut", 30, "Fertilizer"),
        CropSchedule(0, "Groundnut", 60, "Weeding"),
        CropSchedule(0, "Groundnut", 110, "Harvest")
    )

    val fertilizers = listOf(
        Fertilizer(0, "Rice", 20, "Urea"),
        Fertilizer(0, "Rice", 40, "NPK"),
        Fertilizer(0, "Groundnut", 25, "Gypsum"),
        Fertilizer(0, "Maize", 20, "Urea")
    )

    val irrigations = listOf(
        Irrigation(0, "Rice", 5),
        Irrigation(0, "Groundnut", 7),
        Irrigation(0, "Cotton", 10),
        Irrigation(0, "Maize", 6)
    )

    val pests = listOf(
        Pest(0, "Rice", "High humidity", "Fungal disease"),
        Pest(0, "Cotton", "Hot weather", "Bollworm"),
        Pest(0, "Groundnut", "Dry weather", "Aphids")
    )

    val wastes = listOf(
        Waste(0, "Rice Straw", "Compost", listOf("Collect straw", "Layer with cow dung", "Add water", "Turn every 10 days", "Ready in 45 days")),
        Waste(0, "Coconut Husk", "Mulching", listOf("Break into pieces", "Place around plants", "Retains moisture"))
    )

    val terraceFarmingList = listOf(
        TerraceFarming(0, "Tomato", "Full", "Daily", 60),
        TerraceFarming(0, "Spinach", "Partial", "Daily", 30),
        TerraceFarming(0, "Coriander", "Partial", "Daily", 25),
        TerraceFarming(0, "Mint", "Shade", "Daily", 20),
        TerraceFarming(0, "Chili", "Full", "Daily", 70)
    )

    val schemes = listOf(
        Scheme(0, "PM-KISAN (Pradhan Mantri Kisan Samman Nidhi)", "Direct income support of ₹6,000 per year transferred in three equal installments.", "Small and marginal farmer families possessing cultivable landholding up to 2 hectares (approx 5 acres). Excludes institutional landholders and high-income professionals."),
        Scheme(0, "Kisan Credit Card (KCC)", "Flexible, low-interest short-term loans up to ₹3 Lakhs for cultivation, post-harvest expenses, and farm maintenance.", "All active farmers, tenant farmers, oral lessees, and sharecroppers. Requires valid land records, Aadhaar, and basic identity proof."),
        Scheme(0, "PMFBY (Crop Insurance)", "Comprehensive financial protection against non-preventable yield losses due to natural calamities, extreme weather, and pests.", "All farmers growing notified crops in notified areas, including tenant farmers. Mandatory for loanee farmers. Extremely low premium rates (1.5% to 2%)."),
        Scheme(0, "Paramparagat Krishi Vikas Yojana", "Financial assistance of ₹50,000 per hectare over 3 years to cover organic seeds, harvesting, and marketing.", "Groups of farmers adopting organic, pesticide-free agriculture across clusters of 20 hectares. Must be willing to maintain Participatory Guarantee System certification.")
    )
}
