package com.attri.swissarmy.feature.utilities.cleaner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleanerViewModel @Inject constructor(
    private val repository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CleanerUiState())
    val uiState: StateFlow<CleanerUiState> = _uiState.asStateFlow()

    fun startAnalysis() {
        _uiState.update { it.copy(isLoading = true, permissionDenied = false) }
        
        viewModelScope.launch {
            try {
                val analysis = repository.analyzeMessages()
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        totalMessages = analysis.totalMessages,
                        junkCount = analysis.junkMessages,
                        junkGroups = analysis.junkGroups
                    ) 
                }
            } catch (e: Exception) {
               _uiState.update { it.copy(isLoading = false) } // Handle error gracefully
            }
        }
    }
    
    fun deleteJunk() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val deleted = repository.deleteJunkMessages()
            val message = when {
                deleted > 0 -> "Deleted $deleted junk messages!"
                deleted == 0 -> "No junk messages found to delete."
                else -> "Error: Make sure Swiss Army is set as Default SMS App."
            }
            _uiState.update { it.copy(isLoading = false, successMessage = message) }
            startAnalysis() // Refresh
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
    
    fun onPermissionDenied() {
        _uiState.update { it.copy(isLoading = false, permissionDenied = true) }
    }
}

data class CleanerUiState(
    val isLoading: Boolean = false,
    val permissionDenied: Boolean = false,
    val totalMessages: Int = 0,
    val junkCount: Int = 0,
    val junkGroups: List<MessageGroup> = emptyList(),
    val successMessage: String? = null
)
