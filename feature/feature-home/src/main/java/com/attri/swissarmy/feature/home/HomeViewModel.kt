package com.attri.swissarmy.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.TextFields
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    
    val features = listOf(
        FeatureItem("scanner", "Doc Scanner", Icons.Default.CameraAlt, "Scan & Share"),
        FeatureItem("pdf_compress", "PDF Tools", Icons.Default.Compress, "Compress, Convert"),
        FeatureItem("image_tools", "Image Tools", Icons.Default.Image, "Compress, Resize"),
        FeatureItem("sip_calculator", "Calculators", Icons.Default.Calculate, "SIP, Interest"),
        FeatureItem("alarm", "Intl. Alarm", Icons.Default.Alarm, "Timezone Aware"),
        FeatureItem("ascii", "ASCII Table", Icons.Default.TextFields, "Char Codes"),
        FeatureItem("rcs_cleaner", "RCS Cleaner", Icons.Default.Lock, "Clean Messages"),
        FeatureItem("dictionary", "Dictionary", Icons.Default.List, "Hindi <-> Eng")
    )
}
