package com.attri.swissarmy.feature.utilities.cleaner

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attri.swissarmy.core.ui.components.SwissButton
import com.attri.swissarmy.core.ui.components.SwissCard

@Composable
fun CleanerRoute(
    viewModel: CleanerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    CleanerScreen(
        uiState = uiState,
        onStartScan = viewModel::startAnalysis,
        onPermissionDenied = viewModel::onPermissionDenied,
        onDeleteAll = viewModel::deleteJunk,
        onClearMessage = viewModel::clearMessage
    )
}

@Composable
fun CleanerScreen(
    uiState: CleanerUiState,
    onStartScan: () -> Unit,
    onPermissionDenied: () -> Unit,
    onDeleteAll: () -> Unit,
    onClearMessage: () -> Unit
) {
    val context = LocalContext.current
    
    val defaultSmsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // After returning from dialog, check if we are now default
        if (android.provider.Telephony.Sms.getDefaultSmsPackage(context) == context.packageName) {
            onDeleteAll()
        }
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onStartScan()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            onClearMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
             Text(
                text = "Message Cleaner",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Analyze and identify junk messages",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (!uiState.isLoading && uiState.totalMessages == 0 && !uiState.permissionDenied) {
                // Initial State
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Ready to scan your inbox")
                        Spacer(modifier = Modifier.height(24.dp))
                        SwissButton(
                            text = "Start Analysis",
                            onClick = { permissionLauncher.launch(Manifest.permission.READ_SMS) }
                        )
                    }
                }
            } else if (uiState.isLoading) {
                 Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.permissionDenied) {
                 Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text("Permission Required to read messages")
                        SwissButton("Retry", onClick = { permissionLauncher.launch(Manifest.permission.READ_SMS) })
                    }
                }
            } else {
                // Results State
                SwissCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Scanned", style = MaterialTheme.typography.labelMedium)
                            Text("${uiState.totalMessages}", style = MaterialTheme.typography.headlineSmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Junk Detected", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
                            Text("${uiState.junkCount}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Potential Junk Groups", style = MaterialTheme.typography.titleMedium)
                    // Bulk Action
                    SwissButton(
                        text = "Clean All",
                        onClick = { 
                            val isDefault = android.provider.Telephony.Sms.getDefaultSmsPackage(context) == context.packageName
                            if (isDefault) {
                                onDeleteAll()
                            } else {
                                // Prompt to become default
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                    val roleManager = context.getSystemService(android.app.role.RoleManager::class.java)
                                    if (roleManager.isRoleAvailable(android.app.role.RoleManager.ROLE_SMS)) {
                                        defaultSmsLauncher.launch(roleManager.createRequestRoleIntent(android.app.role.RoleManager.ROLE_SMS))
                                    }
                                } else {
                                    val intent = android.content.Intent(android.provider.Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                                    intent.putExtra(android.provider.Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.packageName)
                                    defaultSmsLauncher.launch(intent)
                                }
                            }
                        }
                    )
                }
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.junkGroups) { group ->
                        SwissCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(group.sender, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text(
                                        group.lastMessage, 
                                        style = MaterialTheme.typography.bodySmall, 
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                         Text("${group.count} msgs", style = MaterialTheme.typography.labelSmall)
                                         if (group.isOtp) {
                                            Spacer(modifier = Modifier.size(8.dp))
                                            Text("• OTP", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                                         }
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        // Open SMS App
                                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                            data = android.net.Uri.parse("sms:${group.sender}")
                                        }
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Could not open SMS app", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete, 
                                        contentDescription = "Manage in SMS App",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
