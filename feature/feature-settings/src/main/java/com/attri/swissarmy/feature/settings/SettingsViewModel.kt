package com.attri.swissarmy.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attri.swissarmy.feature.settings.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.themeMode.collectLatest { mode ->
                _uiState.update { it.copy(themeMode = mode) }
            }
        }
        viewModelScope.launch {
             repository.scanQuality.collectLatest { quality ->
                _uiState.update { it.copy(defaultScanQuality = quality) }
            }
        }
        viewModelScope.launch {
             repository.compressionLevel.collectLatest { level ->
                _uiState.update { it.copy(defaultCompressionLevel = level) }
            }
        }
    }

    fun setTheme(mode: String) {
        viewModelScope.launch { repository.setThemeMode(mode) }
    }
    
    fun setScanQuality(quality: Int) {
        viewModelScope.launch { repository.setScanQuality(quality) }
    }
    
    fun setCompressionLevel(level: Int) {
        viewModelScope.launch { repository.setCompressionLevel(level) }
    }

    fun clearCache() {
        viewModelScope.launch {
            val freedBytes = repository.clearCache()
            val message = if (freedBytes >= 0) {
                // Formatting size
                val mb = freedBytes / (1024 * 1024.0)
                "Cache Cleared! Freed ${String.format("%.2f", mb)} MB"
            } else {
                "Failed to clear cache"
            }
            _uiState.update { it.copy(toastMessage = message) }
        }
    }
    
    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}

data class SettingsUiState(
    val themeMode: String = "System", // System, Dark, Light
    val defaultScanQuality: Int = 80,
    val defaultCompressionLevel: Int = 50,
    val toastMessage: String? = null
)
