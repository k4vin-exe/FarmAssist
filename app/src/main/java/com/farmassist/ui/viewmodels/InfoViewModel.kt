package com.farmassist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmassist.data.local.dao.FarmDao
import com.farmassist.data.local.model.Scheme
import com.farmassist.data.local.model.TerraceFarming
import com.farmassist.data.local.model.Waste
import com.farmassist.data.remote.SchemesApi
import com.farmassist.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InfoViewModel(private val dao: FarmDao, private val sessionManager: SessionManager) : ViewModel() {

    private val _terraceFarming = MutableStateFlow<List<TerraceFarming>>(emptyList())
    val terraceFarming: StateFlow<List<TerraceFarming>> = _terraceFarming.asStateFlow()

    private val _myTerraceCrops = MutableStateFlow<Set<String>>(sessionManager.getTerraceCrops())
    val myTerraceCrops: StateFlow<Set<String>> = _myTerraceCrops.asStateFlow()

    private val _myFieldCrops = MutableStateFlow<Set<String>>(sessionManager.getPlantedCrops())
    val myFieldCrops: StateFlow<Set<String>> = _myFieldCrops.asStateFlow()

    fun toggleTerraceCrop(cropName: String, isAdding: Boolean) {
        if (isAdding) sessionManager.addTerraceCrop(cropName)
        else sessionManager.removeTerraceCrop(cropName)
        _myTerraceCrops.value = sessionManager.getTerraceCrops()
    }

    fun getDaysSinceTerracePlanted(cropName: String): Int {
        return sessionManager.getDaysSinceTerracePlanted(cropName)
    }

    private val _wastes = MutableStateFlow<List<Waste>>(emptyList())
    val wastes: StateFlow<List<Waste>> = _wastes.asStateFlow()

    private val _schemes = MutableStateFlow<List<Scheme>>(emptyList())
    val schemes: StateFlow<List<Scheme>> = _schemes.asStateFlow()

    init {
        viewModelScope.launch {
            _terraceFarming.value = dao.getAllTerraceFarming()
            _wastes.value = dao.getAllWastes()
            
            // 1. Instantly load offline-first data (Seeded natively from SQLite)
            _schemes.value = dao.getAllSchemes()

            // 2. Background Network Synchronization Attempt
            try {
                // If the user has network access, reach out to the remote JSON endpoint
                val remoteData = SchemesApi.create().getLatestSchemes()
                
                if (remoteData.isNotEmpty()) {
                    // Map the generic JSON payload into secure Room Local Database Entities
                    val newEntities = remoteData.map { Scheme(0, it.name, it.benefit, it.eligibility) }
                    
                    // Wipe the old local history and overwrite with the fresh Cloud update
                    dao.clearSchemes()
                    dao.insertSchemes(newEntities)
                    
                    // 3. Instantly refresh the User Interface with the newly synced data block
                    _schemes.value = dao.getAllSchemes()
                }
            } catch (e: Exception) {
                // 4. OFFLINE FALLBACK
                // The network is down, or the endpoint 404'd.
                // The app silently suppresses the error and continues displaying the offline data loaded in Step 1.
            }
        }
    }
}
