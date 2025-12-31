package com.attri.swissarmy.feature.pdftools

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.attri.swissarmy.core.ui.components.SwissButton
import com.attri.swissarmy.core.ui.components.SwissGradientCard
import com.attri.swissarmy.core.ui.components.SwissCard

@Composable
fun PdfToolsRoute(
    viewModel: PdfViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    PdfToolsScreen(
        uiState = uiState,
        onFileSelect = viewModel::updateSelectedFile,
        onCompressionLevelChange = viewModel::setCompressionLevel,
        onCompressClick = viewModel::compressPdf,
        onClearMessages = viewModel::clearMessages,
        onToolChange = viewModel::setActiveTool,
        onPasswordChange = viewModel::setPassword,
        onRemovePasswordClick = viewModel::removePassword,
        onRenameFile = viewModel::renameProcessedFile,
        onReset = viewModel::resetToolState
    )
}

@Composable
fun PdfToolsScreen(
    uiState: PdfUiState,
    onFileSelect: (Uri?) -> Unit,
    onCompressionLevelChange: (Float) -> Unit,
    onCompressClick: () -> Unit,
    onClearMessages: () -> Unit,
    onToolChange: (PdfTool) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onRemovePasswordClick: () -> Unit = {},
    onRenameFile: (String) -> Unit = {},
    onReset: () -> Unit = {}
) {
    val context = LocalContext.current
    var showRenameDialog by remember { androidx.compose.runtime.mutableStateOf(false) }
    var renameText by remember { androidx.compose.runtime.mutableStateOf("") }
    
    val pdfPickerLauncher = rememberLauncherForActivityResult(
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
            title = { Text("Rename File") },
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
                    text = "PDF Tools",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Compress, Secure, Convert",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Tools Menu
            if (uiState.activeTool != PdfTool.COMPRESS && uiState.processedPdfUri == null) {
                item {
                   SwissButton(
                        text = "Compress PDF",
                        onClick = { onToolChange(PdfTool.COMPRESS) },
                        primary = false,
                        icon = Icons.Default.Compress
                   )
                }
            }

            if (uiState.activeTool != PdfTool.REMOVE_PASSWORD && uiState.processedPdfUri == null) {
                item {
                   SwissButton(
                        text = "Remove Password",
                        onClick = { onToolChange(PdfTool.REMOVE_PASSWORD) },
                        primary = false,
                        icon = Icons.Default.LockOpen
                   )
                }
            }
            
            // Active Tool UI
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SwissCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = when(uiState.activeTool) {
                                PdfTool.COMPRESS -> "PDF Compressor"
                                PdfTool.REMOVE_PASSWORD -> "Remove Password"
                                else -> "Tool"
                            },
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.processedPdfUri != null) {
                             // Success / Output View
                             Column(
                                 horizontalAlignment = Alignment.CenterHorizontally,
                                 verticalArrangement = Arrangement.spacedBy(12.dp),
                                 modifier = Modifier.fillMaxWidth()
                             ) {
                                 Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                                 Text(
                                     text = "Success!",
                                     style = MaterialTheme.typography.headlineSmall,
                                     color = MaterialTheme.colorScheme.primary
                                 )
                                 Text(
                                     text = uiState.processedPdfUri?.lastPathSegment ?: "output.pdf",
                                     style = MaterialTheme.typography.bodyMedium
                                 )
                                 
                                 Spacer(modifier = Modifier.height(16.dp))
                                 
                                 Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                     SwissButton(
                                         text = "Open",
                                         onClick = {
                                             val processedUri = uiState.processedPdfUri
                                             if (processedUri != null) {
                                                try {
                                                     val file = java.io.File(processedUri.path!!)
                                                     val contentUri = androidx.core.content.FileProvider.getUriForFile(
                                                         context,
                                                         "${context.packageName}.fileprovider",
                                                         file
                                                     )
                                                     val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                                         setDataAndType(contentUri, "application/pdf")
                                                         flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                     }
                                                     context.startActivity(intent)
                                                } catch(e: Exception) {
                                                     Toast.makeText(context, "Cannot open file: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                             }
                                         },
                                         modifier = Modifier.weight(1f)
                                     )
                                     SwissButton(
                                         text = "Share",
                                         onClick = {
                                             val processedUri = uiState.processedPdfUri
                                             if (processedUri != null) {
                                                try {
                                                     val file = java.io.File(processedUri.path!!)
                                                     val contentUri = androidx.core.content.FileProvider.getUriForFile(
                                                         context,
                                                         "${context.packageName}.fileprovider",
                                                         file
                                                     )
                                                     val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                                         type = "application/pdf"
                                                         putExtra(android.content.Intent.EXTRA_STREAM, contentUri)
                                                         flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                                     }
                                                     context.startActivity(android.content.Intent.createChooser(intent, "Share PDF"))
                                                } catch(e: Exception) {
                                                     Toast.makeText(context, "Cannot share file: ${e.message}", Toast.LENGTH_SHORT).show()
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
                                             val currentName = java.io.File(uiState.processedPdfUri?.path ?: "").name
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
                        } else if (uiState.selectedFileUri == null) {
                            SwissButton(
                                text = "Select PDF",
                                onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                                icon = Icons.Default.UploadFile
                            )
                        } else {
                            Text(
                                text = "Selected: ...${uiState.selectedFileUri.path?.takeLast(25)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            when(uiState.activeTool) {
                                PdfTool.COMPRESS -> {
                                    Text(text = "Compression Level: ${(uiState.compressionLevel * 100).toInt()}%")
                                    Slider(
                                        value = uiState.compressionLevel,
                                        onValueChange = onCompressionLevelChange,
                                        valueRange = 0f..1f
                                    )
                                }
                                PdfTool.REMOVE_PASSWORD -> {
                                    androidx.compose.material3.OutlinedTextField(
                                        value = uiState.password,
                                        onValueChange = onPasswordChange,
                                        label = { Text("Enter Password") },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                                    )
                                }
                                else -> {}
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (uiState.isProcessing) {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                Text(
                                    text = "Processing...",
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    SwissButton(
                                        text = if (uiState.activeTool == PdfTool.COMPRESS) "Compress Now" else "Unlock Now", 
                                        onClick = if (uiState.activeTool == PdfTool.COMPRESS) onCompressClick else onRemovePasswordClick,
                                        modifier = Modifier.weight(1f)
                                    )
                                    SwissButton(
                                        text = "Change File", 
                                        onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
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

@Composable
fun PdfToolItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    SwissGradientCard(
        onClick = onClick,
        modifier = Modifier.height(100.dp)
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = description, 
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
