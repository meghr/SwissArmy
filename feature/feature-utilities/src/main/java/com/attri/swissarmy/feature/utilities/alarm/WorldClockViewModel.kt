package com.attri.swissarmy.feature.utilities.alarm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

@HiltViewModel
class WorldClockViewModel @Inject constructor(
    private val repository: WorldClockRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorldClockUiState())
    val uiState: StateFlow<WorldClockUiState> = _uiState.asStateFlow()

    init {
        loadRegions()
        startClockTick()
    }

    private fun loadRegions() {
        val hierarchy = repository.getWorldTimeHierarchy()
        _uiState.update { 
            it.copy(
                regions = hierarchy.keys.sorted(),
                fullHierarchy = hierarchy
            ) 
        }
    }

    private fun startClockTick() {
        viewModelScope.launch {
            while(true) {
                // Update time for the selected city if one is selected
                _uiState.value.selectedZoneId?.let { zoneId ->
                   val time = repository.getTimeForZone(zoneId)
                   val date = repository.getDateForZone(zoneId)
                   _uiState.update { it.copy(currentTime = time, currentDate = date) }
                }
                delay(1000)
            }
        }
    }

    fun selectRegion(region: String) {
        val hierarchy = _uiState.value.fullHierarchy
        val cities = hierarchy[region] ?: emptyList()
        
        _uiState.update { 
            it.copy(
                selectedRegion = region,
                filteredCities = cities,
                selectedZoneId = null, // Reset city selection
                currentTime = null
            ) 
        }
    }

    fun selectCity(zoneId: String) {
        val time = repository.getTimeForZone(zoneId)
        val date = repository.getDateForZone(zoneId)
        _uiState.update { 
            it.copy(
                selectedZoneId = zoneId,
                currentTime = time,
                currentDate = date
            ) 
        }
    }
    
    fun resetSelection() {
        _uiState.update { 
             it.copy(selectedRegion = null, selectedZoneId = null, currentTime = null)
        }
    }
}

data class WorldClockUiState(
    val regions: List<String> = emptyList(),
    val fullHierarchy: Map<String, List<String>> = emptyMap(), // Region -> List<ZoneId>
    
    val selectedRegion: String? = null, // Step 1: Region Selected
    val filteredCities: List<String> = emptyList(), // Cities in selected region
    
    val selectedZoneId: String? = null, // Step 2: City Selected
    val currentTime: String? = null,
    val currentDate: String? = null
)
