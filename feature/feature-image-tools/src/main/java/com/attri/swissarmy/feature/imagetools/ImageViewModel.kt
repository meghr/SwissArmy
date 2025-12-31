package com.attri.swissarmy.feature.imagetools

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.attri.swissarmy.feature.imagetools.domain.ImageToolsRepository
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
class ImageViewModel @Inject constructor(
    private val repository: ImageToolsRepository,
    private val settingsRepository: com.attri.swissarmy.feature.settings.domain.SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState())
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.scanQuality.collect { quality ->
                _uiState.update { it.copy(quality = quality) }
            }
        }
    }

    fun updateSelectedImage(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri, processingError = null, successMessage = null) }
    }

    fun setQuality(quality: Int) {
        _uiState.update { it.copy(quality = quality) }
    }

    fun compressImage() {
        val uri = uiState.value.selectedImageUri ?: return
        val quality = uiState.value.quality
        
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, processedImageUri = null) }
            try {
                val resultFile = repository.compressImage(uri, quality)
                _uiState.update { 
                    it.copy(
                        isProcessing = false, 
                        successMessage = "Image Compressed!",
                        processedImageUri = Uri.fromFile(resultFile)
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

    fun renameProcessedFile(newName: String) {
        val currentUri = uiState.value.processedImageUri ?: return
        val currentFile = java.io.File(currentUri.path ?: return)
        
        // Ensure extension
        val validName = if (newName.endsWith(".jpg", true) || newName.endsWith(".jpeg", true) || newName.endsWith(".png", true)) newName else "$newName.jpg"
        val newFile = java.io.File(currentFile.parent, validName)
        
        if (currentFile.renameTo(newFile)) {
            _uiState.update { 
                it.copy(
                    processedImageUri = Uri.fromFile(newFile),
                    successMessage = "Renamed to $validName"
                ) 
            }
        } else {
             _uiState.update { it.copy(processingError = "Could not rename file") }
        }
    }
    
    fun resetState() {
        _uiState.update { it.copy(selectedImageUri = null, processedImageUri = null, successMessage = null, processingError = null) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(processingError = null, successMessage = null) }
    }
}

data class ImageUiState(
    val selectedImageUri: Uri? = null,
    val isProcessing: Boolean = false,
    val processingError: String? = null,
    val successMessage: String? = null,
    val quality: Int = 80, // 0 to 100
    val processedImageUri: Uri? = null
)
