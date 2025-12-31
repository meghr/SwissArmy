package com.attri.swissarmy.feature.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attri.swissarmy.feature.dictionary.domain.DictionaryEntry
import com.attri.swissarmy.feature.dictionary.domain.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        
        // Debounce search to avoid too many API calls
        searchJob?.cancel()
        
        if (query.length > 1) {
            searchJob = viewModelScope.launch {
                delay(500) // Wait 500ms before searching
                _uiState.update { it.copy(isLoading = true, error = null) }
                try {
                    val results = repository.search(query)
                    _uiState.update { it.copy(results = results, isLoading = false) }
                } catch (e: Exception) {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = "Failed to fetch: ${e.message}",
                            results = emptyList()
                        ) 
                    }
                }
            }
        } else {
            _uiState.update { it.copy(results = emptyList(), isLoading = false) }
        }
    }
}

data class DictionaryUiState(
    val query: String = "",
    val results: List<DictionaryEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
