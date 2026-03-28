package com.farmassist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmassist.data.local.dao.FarmDao
import com.farmassist.data.local.model.Crop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CostYieldViewModel(private val dao: FarmDao) : ViewModel() {
    private val _crops = MutableStateFlow<List<Crop>>(emptyList())
    val crops: StateFlow<List<Crop>> = _crops.asStateFlow()

    init {
        viewModelScope.launch {
            _crops.value = dao.getAllCrops()
        }
    }

    fun calculate(crop: Crop, acres: Double): Pair<Double, Double> {
        val totalCost = crop.cost_per_acre * acres
        val totalYield = crop.yield_per_acre * acres
        return Pair(totalCost, totalYield)
    }
}
