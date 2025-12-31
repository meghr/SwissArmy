package com.attri.swissarmy.feature.scanner

import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor() : ViewModel() {
    
    fun onScanResult(result: GmsDocumentScanningResult) {
        // Handle scan result
    }
}
