package com.attri.swissarmy.feature.pdftools

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfViewModel @Inject constructor(
    private val repository: com.attri.swissarmy.feature.pdftools.domain.PdfToolsRepository,
    private val settingsRepository: com.attri.swissarmy.feature.settings.domain.SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfUiState())
    val uiState: StateFlow<PdfUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.compressionLevel.collect { level ->
                _uiState.update { it.copy(compressionLevel = level / 100f) }
            }
        }
    }

    private val _events = MutableSharedFlow<PdfEvent>()
    val events: SharedFlow<PdfEvent> = _events.asSharedFlow()

    fun updateSelectedFile(uri: Uri?) {
        _uiState.update { it.copy(selectedFileUri = uri, processingError = null, successMessage = null) }
    }

    fun setCompressionLevel(level: Float) {
        _uiState.update { it.copy(compressionLevel = level) }
    }

    fun setPassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }
    
    fun setActiveTool(tool: PdfTool) {
        _uiState.update { it.copy(activeTool = tool, selectedFileUri = null, successMessage = null, processingError = null) }
    }

    fun compressPdf() {
        val uri = uiState.value.selectedFileUri ?: return
        val level = uiState.value.compressionLevel
        
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, processedPdfUri = null) }
            try {
                val resultFile = repository.compressPdf(uri, level)
                _uiState.update { 
                    it.copy(
                        isProcessing = false, 
                        successMessage = "PDF Compressed!",
                        processedPdfUri = Uri.fromFile(resultFile)
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isProcessing = false, 
                        processingError = e.message ?: "Unknown error occurred"
                    ) 
                }
            }
        }
    }

    fun removePassword() {
        val uri = uiState.value.selectedFileUri ?: return
        val password = uiState.value.password 
        
        if (password.isBlank()) {
           _uiState.update { it.copy(processingError = "Please enter the password") }
           return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, processedPdfUri = null) }
            try {
                val resultFile = repository.removePassword(uri, password)
                _uiState.update { 
                    it.copy(
                        isProcessing = false, 
                        successMessage = "Password Removed!",
                        processedPdfUri = Uri.fromFile(resultFile)
                    ) 
                }
            } catch (e: Exception) {
                 _uiState.update { 
                    it.copy(
                        isProcessing = false, 
                        processingError = "Failed: ${e.message} (Wrong Password?)"
                    ) 
                }
            }
        }
    }

    fun renameProcessedFile(newName: String) {
        val currentUri = uiState.value.processedPdfUri ?: return
        val currentFile = java.io.File(currentUri.path ?: return)
        
        // Ensure extension
        val validName = if (newName.endsWith(".pdf", true)) newName else "$newName.pdf"
        val newFile = java.io.File(currentFile.parent, validName)
        
        if (currentFile.renameTo(newFile)) {
            _uiState.update { 
                it.copy(
                    processedPdfUri = Uri.fromFile(newFile),
                    successMessage = "Renamed to $validName"
                ) 
            }
        } else {
             _uiState.update { it.copy(processingError = "Could not rename file") }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(processingError = null, successMessage = null) }
    }
    
    fun resetToolState() {
         _uiState.update { it.copy(processedPdfUri = null, selectedFileUri = null, successMessage = null) }
    }
}

enum class PdfTool {
    COMPRESS, REMOVE_PASSWORD, CONVERT
}

data class PdfUiState(
    val activeTool: PdfTool = PdfTool.COMPRESS,
    val selectedFileUri: Uri? = null,
    val isProcessing: Boolean = false,
    val processingError: String? = null,
    val successMessage: String? = null,
    val compressionLevel: Float = 0.5f,
    val password: String = "",
    val processedPdfUri: Uri? = null
)

sealed interface PdfEvent {
    data class ShowToast(val message: String) : PdfEvent
}
