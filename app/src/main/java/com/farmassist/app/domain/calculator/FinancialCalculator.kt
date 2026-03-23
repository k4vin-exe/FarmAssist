package com.farmassist.app.domain.calculator

object FinancialCalculator {
    fun calculateCost(costPerAcre: Int, landSizeInAcres: Double): Double {
        return costPerAcre * landSizeInAcres
    }

    fun calculateYield(yieldPerAcre: Int, landSizeInAcres: Double): Double {
        return yieldPerAcre * landSizeInAcres
    }
}
