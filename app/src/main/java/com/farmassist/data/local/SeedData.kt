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
        Waste(0, "Rice Straw", "Compost / Biogas", listOf("Collect dry straw after harvest", "Layer with cow dung (1:3 ratio)", "Sprinkle water to maintain moisture", "Turn the pile every 10 days", "Ready as rich compost in 45 days", "Alternatively, feed into biogas plant for clean cooking fuel")),
        Waste(0, "Coconut Husk", "Coir / Mulching / Cocopeat", listOf("Soak husks in water for 3–6 months", "Beat to separate fibres (coir) from pith", "Coir for ropes, mats, and erosion control", "Compress pith into cocopeat blocks for nurseries", "Use raw husk as moisture-retaining mulch")),
        Waste(0, "Sugarcane Bagasse", "Biofuel / Cardboard", listOf("Collect crushed bagasse from mills", "Dry in sun for 2–3 days", "Use directly as boiler fuel in sugar factories", "Press into boards as eco-friendly cardboard alternative", "Mix into compost for carbon-rich amendment")),
        Waste(0, "Banana Stem", "Fibre / Animal Feed", listOf("Cut stem into small pieces after fruit harvest", "Extract juicy inner core for cattle feed supplement", "Ret outer fibre in water for 10 days", "Dry and spin into banana fibre yarn", "Remaining pulp added to compost pit")),
        Waste(0, "Cotton Stalk", "Charcoal / Mulch", listOf("Uproot stalks after harvest season", "Shred or chip into small pieces", "Use as mulch layer around fruit trees", "Pyrolyse at low oxygen to produce biochar", "Biochar improves water retention and soil fertility"))
    )

    val terraceFarmingList = listOf(
        TerraceFarming(0, "Tomato", "Full", "Daily", 60),
        TerraceFarming(0, "Spinach", "Partial", "Daily", 30),
        TerraceFarming(0, "Coriander", "Partial", "Daily", 25),
        TerraceFarming(0, "Mint", "Shade", "Daily", 20),
        TerraceFarming(0, "Chili", "Full", "Daily", 70),
        TerraceFarming(0, "Fenugreek", "Partial", "Daily", 25),
        TerraceFarming(0, "Brinjal", "Full", "Daily", 75),
        TerraceFarming(0, "Drumstick", "Full", "Weekly", 180),
        TerraceFarming(0, "Curry Leaf", "Shade", "Weekly", 90),
        TerraceFarming(0, "Bitter Gourd", "Full", "Daily", 65)
    )

    val schemes = listOf(
        Scheme(
            0, "PM-KISAN — Pradhan Mantri Kisan Samman Nidhi",
            "Direct income support of ₹6,000 per year paid in 3 equal installments of ₹2,000 directly into the farmer's bank account (Jan–Apr, May–Aug, Sep–Dec).",
            "• Small & marginal farmers owning cultivable land up to 2 hectares (≈5 acres).\n• All members of the farmer's family (husband, wife, minor children) together must not own more than 2 ha.\n• Must have Aadhaar-linked bank account.\n• Excludes: Former/current MPs, MLAs, Ministers. Income Tax payers. Government employees. Professionals (doctors, engineers, lawyers, CAs).\n• Landholding proof (Patta/ROR) required at time of registration."
        ),
        Scheme(
            0, "Kisan Credit Card (KCC)",
            "Revolving short-term credit of up to ₹3 Lakhs at only 4% interest (after 2% government subvention). Covers seeds, fertilizers, pesticides, post-harvest expenses, and allied activities.",
            "• All farmers — owner-cultivators, joint borrowers, tenant farmers, oral lessees, and sharecroppers.\n• Must have a valid land record or lease agreement.\n• Age: 18–75 years. Above 60 needs a co-applicant.\n• KCC limit is linked to cultivable land area, cropping intensity, and scale of finance per district.\n• Documents: Aadhaar, PAN/Form 60, land records, passport photo.\n• Renewed annually; 5-year validity with annual review."
        ),
        Scheme(
            0, "PMFBY — Pradhan Mantri Fasal Bima Yojana (Crop Insurance)",
            "Full financial compensation for crop losses due to natural disasters (floods, drought, hailstorm, cyclone), prevented sowing, mid-season adversity, and post-harvest losses up to 14 days. Government pays 95–98% of the premium.",
            "• All farmers growing notified crops in notified areas.\n• Premium: Only 1.5% for Kharif crops, 2% for Rabi crops, 5% for commercial/horticultural crops — rest paid by government.\n• Mandatory for loanee farmers who availed crop loans from banks.\n• Voluntary for non-loanee farmers.\n• Enrollment window: 2 weeks before the crop cut-off date.\n• Claim triggered automatically when district-level yield falls below threshold — no individual application needed."
        ),
        Scheme(
            0, "Paramparagat Krishi Vikas Yojana (PKVY)",
            "₹50,000 per hectare over 3 years to adopt 100% organic farming — covering organic seed, biofertilizer, vermicompost, harvest, packaging, and direct-to-market sales support.",
            "• Groups of at least 20 farmers forming a cluster of 20–50 hectares.\n• Must commit to fully pesticide-free, chemical-free cultivation.\n• Farmers must adopt Participatory Guarantee System (PGS) certification process.\n• Priority given to SC/ST farmers and women farmer groups.\n• Cluster registered with the State Agriculture Department.\n• Benefits disbursed in 3 tranches over 3 years upon verified compliance."
        ),
        Scheme(
            0, "PM-Kisan Maan Dhan Yojana (PM-KMY Pension)",
            "Guaranteed pension of ₹3,000 per month upon attaining age 60. Government matches the farmer's own monthly contribution equally.",
            "• Small and marginal farmers aged 18–40 years at enrollment.\n• Monthly contribution ranges from ₹55 (age 18) to ₹200 (age 40) — matched 1:1 by Government of India.\n• Must not be enrolled in any other Central Government pension scheme.\n• Must have PM-KISAN registration and Aadhaar-linked bank account with auto-debit enabled.\n• In case of death of enrolled farmer, spouse receives 50% family pension (₹1,500/month)."
        ),
        Scheme(
            0, "Soil Health Card Scheme",
            "Free, lab-tested Soil Health Card every 2 years indicating exact NPK levels, pH, micronutrients, and personalised crop-specific fertilizer prescription — helping reduce input costs by 8–10%.",
            "• All farmers across India are eligible regardless of land size.\n• Soil sample collected by Agriculture Department officials from the farmer's field.\n• Card issued within 3 months of sample collection.\n• Recommendations provided for 3 consecutive Kharif and Rabi seasons.\n• Farmers must register at soilhealth.dac.gov.in or visit nearest Krishi Vigyan Kendra (KVK).\n• No cost to the farmer — fully funded by the Central Government."
        ),
        Scheme(
            0, "MIDH — Mission for Integrated Development of Horticulture",
            "Capital subsidies of 40–50% for setting up polyhouses, drip irrigation, cold storage units, pack houses, and establishing fruit orchards. Technology missions for coconut, banana, and spices.",
            "• Farmers and farmer producer organisations (FPOs) growing fruits, vegetables, flowers, spices, mushroom, and honey.\n• Subsidy: 40% for general farmers, 50% for NE states, hilly areas, SC/ST farmers.\n• Projects above ₹25 lakh require credit-linked bank financing.\n• Implemented through State Horticulture Missions — apply at district Horticulture Office.\n• Area of cultivation, project report, and land ownership documents required."
        ),
        Scheme(
            0, "eNAM — National Agriculture Market",
            "Sell crops digitally to buyers across India via the eNAM online trading platform — eliminating local middlemen, ensuring transparent price discovery, and enabling direct payment to bank account within 24 hours.",
            "• Any farmer with produce registered under eNAM-linked APMC market.\n• Must have: Aadhaar, active bank account, registered mobile number.\n• Produce must meet minimum quality standards (assaying/grading at the mandi).\n• Currently covers 1,260+ mandis and 200 commodities across 22 states.\n• Register at enam.gov.in or at the nearest eNAM-linked APMC mandi office.\n• No registration fee. Platform handles logistics, quality testing, and digital payments."
        )
    )
}
