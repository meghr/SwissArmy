package com.attri.swissarmy.feature.imagetools

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.attri.swissarmy.core.ui.components.SwissButton
import com.attri.swissarmy.core.ui.components.SwissCard

@Composable
fun ImageToolsRoute(
    viewModel: ImageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    ImageToolsScreen(
        uiState = uiState,
        onFileSelect = viewModel::updateSelectedImage,
        onQualityChange = { viewModel.setQuality(it.toInt()) },
        onCompressClick = viewModel::compressImage,
        onClearMessages = viewModel::clearMessages,
        onRenameFile = viewModel::renameProcessedFile,
        onReset = viewModel::resetState
    )
}

@Composable
fun ImageToolsScreen(
    uiState: ImageUiState,
    onFileSelect: (Uri?) -> Unit,
    onQualityChange: (Float) -> Unit,
    onCompressClick: () -> Unit,
    onClearMessages: () -> Unit,
    onRenameFile: (String) -> Unit = {},
    onReset: () -> Unit = {}
) {
    val context = LocalContext.current
    var showRenameDialog by remember { androidx.compose.runtime.mutableStateOf(false) }
    var renameText by remember { androidx.compose.runtime.mutableStateOf("") }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        onFileSelect(uri)
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            onClearMessages()
        }
    }

    LaunchedEffect(uiState.processingError) {
        uiState.processingError?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            onClearMessages()
        }
    }
    
    // Rename Dialog
    if (showRenameDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Image") },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text("New Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        if (renameText.isNotBlank()) {
                            onRenameFile(renameText)
                            showRenameDialog = false
                        }
                    }
                ) { Text("Rename") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showRenameDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Image Tools",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Compress & Resize",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                SwissCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Image Compressor",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.processedImageUri != null) {
                            // Success / Output View
                             Column(
                                 horizontalAlignment = Alignment.CenterHorizontally,
                                 verticalArrangement = Arrangement.spacedBy(12.dp),
                                 modifier = Modifier.fillMaxWidth()
                             ) {
                                 // Preview processed image
                                 AsyncImage(
                                     model = uiState.processedImageUri,
                                     contentDescription = "Processed Image",
                                     modifier = Modifier.fillMaxWidth().height(200.dp)
                                 )
                                 
                                 Text(
                                     text = "Success!",
                                     style = MaterialTheme.typography.headlineSmall,
                                     color = MaterialTheme.colorScheme.primary
                                 )
                                 Text(
                                     text = uiState.processedImageUri?.lastPathSegment ?: "output.jpg",
                                     style = MaterialTheme.typography.bodyMedium
                                 )
                                 
                                 Spacer(modifier = Modifier.height(16.dp))
                                 
                                 Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                     SwissButton(
                                         text = "Open",
                                         onClick = {
                                             val processedUri = uiState.processedImageUri
                                             if (processedUri != null) {
                                                try {
                                                     val file = java.io.File(processedUri.path!!)
                                                     val contentUri = androidx.core.content.FileProvider.getUriForFile(
                                                         context,
                                                         "${context.packageName}.fileprovider",
                                                         file
                                                     )
                                                     val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                                         setDataAndType(contentUri, "image/*")
                                                         flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                     }
                                                     context.startActivity(intent)
                                                } catch(e: Exception) {
                                                     Toast.makeText(context, "Cannot open: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                             }
                                         },
                                         modifier = Modifier.weight(1f)
                                     )
                                     SwissButton(
                                         text = "Share",
                                         onClick = {
                                             val processedUri = uiState.processedImageUri
                                             if (processedUri != null) {
                                                try {
                                                     val file = java.io.File(processedUri.path!!)
                                                     val contentUri = androidx.core.content.FileProvider.getUriForFile(
                                                         context,
                                                         "${context.packageName}.fileprovider",
                                                         file
                                                     )
                                                     val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                                         type = "image/*"
                                                         putExtra(android.content.Intent.EXTRA_STREAM, contentUri)
                                                         flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                     }
                                                     context.startActivity(android.content.Intent.createChooser(intent, "Share Image"))
                                                } catch(e: Exception) {
                                                     Toast.makeText(context, "Cannot share: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                             }
                                         },
                                         modifier = Modifier.weight(1f),
                                         primary = false
                                     )
                                 }
                                 Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                     SwissButton(
                                         text = "Rename",
                                         onClick = {
                                             val currentName = java.io.File(uiState.processedImageUri?.path ?: "").name
                                             renameText = currentName
                                             showRenameDialog = true
                                         },
                                         modifier = Modifier.weight(1f),
                                         primary = false
                                     )
                                     SwissButton(
                                         text = "Close",
                                         onClick = onReset,
                                         modifier = Modifier.weight(1f),
                                         primary = false
                                     )
                                 }
                             }
                        } else if (uiState.selectedImageUri == null) {
                            SwissButton(
                                text = "Select Image",
                                onClick = { imagePickerLauncher.launch(arrayOf("image/*")) },
                                icon = Icons.Default.UploadFile
                            )
                        } else {
                            // Preview
                            AsyncImage(
                                model = uiState.selectedImageUri,
                                contentDescription = "Selected Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(text = "Quality: ${uiState.quality}%")
                            Slider(
                                value = uiState.quality.toFloat(),
                                onValueChange = onQualityChange,
                                valueRange = 10f..100f
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (uiState.isProcessing) {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                Text(
                                    text = "Compressing...",
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    SwissButton(
                                        text = "Compress Now", 
                                        onClick = onCompressClick,
                                        modifier = Modifier.weight(1f)
                                    )
                                    SwissButton(
                                        text = "Change", 
                                        onClick = { imagePickerLauncher.launch(arrayOf("image/*")) },
                                        primary = false,
                                        modifier = Modifier.weight(1f)
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
