package com.attri.swissarmy.feature.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attri.swissarmy.core.ui.components.SwissCard

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    SettingsScreen(
        uiState = uiState,
        onBack = onBack,
        onThemeChange = viewModel::setTheme,
        onQualityChange = viewModel::setScanQuality,
        onCompressionChange = viewModel::setCompressionLevel,
        onClearCache = viewModel::clearCache,
        onClearMessage = viewModel::clearToast
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onBack: () -> Unit,
    onThemeChange: (String) -> Unit,
    onQualityChange: (Int) -> Unit,
    onCompressionChange: (Int) -> Unit,
    onClearCache: () -> Unit,
    onClearMessage: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            onClearMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // Appearance Section
            SettingsSection(title = "Appearance", icon = Icons.Default.BrightnessMedium) {
                Text("Theme", style = MaterialTheme.typography.titleSmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.themeMode == "System", 
                        onClick = { onThemeChange("System") }
                    )
                    Text("System Default")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.themeMode == "Dark", 
                        onClick = { onThemeChange("Dark") }
                    )
                    Text("Dark Mode")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = uiState.themeMode == "Light", 
                        onClick = { onThemeChange("Light") }
                    )
                    Text("Light Mode")
                }
            }
            
            // Defaults Section
            SettingsSection(title = "Defaults", icon = Icons.Default.Image) {
                 Text("Default Scan Quality: ${uiState.defaultScanQuality}%")
                 Slider(
                     value = uiState.defaultScanQuality.toFloat(),
                     onValueChange = { onQualityChange(it.toInt()) },
                     valueRange = 50f..100f
                 )
                 
                 Text("Default Compression: ${uiState.defaultCompressionLevel}%")
                 Slider(
                     value = uiState.defaultCompressionLevel.toFloat(),
                     onValueChange = { onCompressionChange(it.toInt()) },
                     valueRange = 10f..90f
                 )
            }

            // Storage Section
            SettingsSection(title = "Storage", icon = Icons.Default.Delete) {
                 Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClearCache() }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Clear App Cache", style = MaterialTheme.typography.titleMedium)
                        Text("Free up space manually", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                }
            }
            
            // About Section
            SettingsSection(title = "About", icon = Icons.Default.Info) {
                Text("Version: 1.0.0 (Debug)", style = MaterialTheme.typography.bodyMedium)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                     verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Text("Rate this App", modifier = Modifier.padding(start = 8.dp))
                }
            }
            
            // Bottom Padding
            Spacer(modifier = Modifier.padding(bottom = 32.dp))
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        SwissCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}
