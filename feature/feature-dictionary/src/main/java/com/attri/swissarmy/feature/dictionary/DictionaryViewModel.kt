package com.attri.swissarmy.feature.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attri.swissarmy.feature.dictionary.domain.DictionaryEntry
import com.attri.swissarmy.feature.dictionary.domain.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        if (query.length > 1) {
             viewModelScope.launch {
                 val results = repository.search(query)
                 _uiState.update { it.copy(results = results) }
             }
        } else {
             _uiState.update { it.copy(results = emptyList()) }
        }
    }
}

data class DictionaryUiState(
    val query: String = "",
    val results: List<DictionaryEntry> = emptyList()
)
