package com.attri.swissarmy.feature.utilities.alarm
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attri.swissarmy.core.ui.components.SwissCard

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun WorldClockRoute(
    viewModel: WorldClockViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // State for Time Picker
    var showTimePicker by remember { mutableStateOf(false) }
    var pickedHour by remember { mutableIntStateOf(0) }
    var pickedMinute by remember { mutableIntStateOf(0) }
    val timePickerState = androidx.compose.material3.rememberTimePickerState()

    if (showTimePicker) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                        viewModel.uiState.value.selectedZoneId?.let { zoneId ->
                             // 1. Target Time in Remote Zone
                             val remoteZone = java.time.ZoneId.of(zoneId)
                             val localZone = java.time.ZoneId.systemDefault()
                             val nowRemote = java.time.ZonedDateTime.now(remoteZone)
                             
                             // User picked time for 'Today' in remote zone
                             val targetRemote = nowRemote.withHour(timePickerState.hour).withMinute(timePickerState.minute).withSecond(0)
                             
                             // 2. Convert to System Local Time for Alarm Manager
                             val targetLocal = targetRemote.withZoneSameInstant(localZone)
                             
                             val hourLocal = targetLocal.hour
                             val minLocal = targetLocal.minute
                             
                             val intent = android.content.Intent(android.provider.AlarmClock.ACTION_SET_ALARM).apply {
                                putExtra(android.provider.AlarmClock.EXTRA_HOUR, hourLocal)
                                putExtra(android.provider.AlarmClock.EXTRA_MINUTES, minLocal)
                                putExtra(android.provider.AlarmClock.EXTRA_MESSAGE, "Alarm for $zoneId")
                                putExtra(android.provider.AlarmClock.EXTRA_SKIP_UI, false)
                             }
                             try {
                                 context.startActivity(intent)
                             } catch (e: Exception) {
                                 android.widget.Toast.makeText(context, "Alarm App not found", android.widget.Toast.LENGTH_SHORT).show()
                             }
                        }
                    }
                ) { Text("Set Alarm") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = {
                androidx.compose.material3.TimePicker(state = timePickerState)
            }
        )
    }

    WorldClockScreen(
        uiState = uiState,
        onRegionSelect = viewModel::selectRegion,
        onCitySelect = viewModel::selectCity,
        onBack = viewModel::resetSelection,
        onSetAlarmClick = { showTimePicker = true }
    )
}

@Composable
fun WorldClockScreen(
    uiState: WorldClockUiState,
    onRegionSelect: (String) -> Unit,
    onCitySelect: (String) -> Unit,
    onBack: () -> Unit,
    onSetAlarmClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (uiState.selectedRegion != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = uiState.selectedRegion,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            } else {
                 Text(
                    text = "World Clock",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            
            if (uiState.selectedRegion == null) {
                // Step 1: Region List
                Text(
                    text = "Select Region",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.regions) { region ->
                        SwissCard(
                            modifier = Modifier.clickable { onRegionSelect(region) }
                        ) {
                            Text(
                                text = region,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            } else if (uiState.selectedZoneId == null) {
                // Step 2: City List
                 Text(
                    text = "Select City / Location",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filteredCities) { zoneId ->
                        // zoneId is typically "Region/City" or "Region/Country/City"
                        // Display detailed name
                        val displayName = zoneId.substringAfter("${uiState.selectedRegion}/").replace("_", " ")
                        
                        SwissCard(
                            modifier = Modifier.clickable { onCitySelect(zoneId) }
                        ) {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            } else {
                // Step 3: Clock Display (Detail View)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    SwissCard {
                         Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                             Text(
                                text = uiState.selectedZoneId.substringAfterLast("/").replace("_", " "),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Text(
                                text = uiState.currentTime ?: "--:--",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Text(
                                text = uiState.currentDate ?: "",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    
                    // Alarm Action
                    com.attri.swissarmy.core.ui.components.SwissButton(
                        text = "Set Alarm",
                        onClick = onSetAlarmClick
                    )
                    
                    SwissCard(
                         modifier = Modifier.clickable { onBack() }
                    ) {
                         Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.AccessTime, contentDescription = null)
                            Text(
                                text = "Select Another Location",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
