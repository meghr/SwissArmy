package com.attri.swissarmy.feature.scanner

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.attri.swissarmy.core.ui.components.SwissButton
import com.attri.swissarmy.core.ui.components.SwissCard
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

@Composable
fun ScannerRoute(
    viewModel: ScannerViewModel = hiltViewModel()
) {
    ScannerScreen(
        onScanResult = viewModel::onScanResult
    )
}

@Composable
fun ScannerScreen(
    onScanResult: (GmsDocumentScanningResult) -> Unit
) {
    val context = LocalContext.current
    var scanResult by remember { mutableStateOf<GmsDocumentScanningResult?>(null) }
    var currentPdfUri by remember { mutableStateOf<Uri?>(null) }
    
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf("") }
    
    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val gmsResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            gmsResult?.let {
                scanResult = it
                val originalUri = it.pdf?.uri
                if (originalUri != null) {
                    try {
                        // Automatically copy to internal storage to own the file and allow sharing/opening
                        val fileName = "Scan_${System.currentTimeMillis()}.pdf"
                        val file = java.io.File(context.filesDir, fileName)
                        context.contentResolver.openInputStream(originalUri)?.use { input ->
                            file.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        // Use our own FileProvider URI
                        currentPdfUri = androidx.core.content.FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )
                        onScanResult(it) // Inform ViewModel if needed
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error saving scan: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // Rename Dialog logic
    if (showRenameDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Save As (Rename)") },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text("File Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        if (renameText.isNotBlank() && currentPdfUri != null) {
                            try {
                                val name = if (renameText.endsWith(".pdf", true)) renameText else "$renameText.pdf"
                                val newFile = java.io.File(context.filesDir, name)
                                
                                context.contentResolver.openInputStream(currentPdfUri!!)?.use { input ->
                                    newFile.outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                currentPdfUri = androidx.core.content.FileProvider.getUriForFile(
                                    context, 
                                    "${context.packageName}.fileprovider", 
                                    newFile
                                )
                                Toast.makeText(context, "Saved as $name", Toast.LENGTH_SHORT).show()
                            } catch(e: Exception) {
                                Toast.makeText(context, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                            showRenameDialog = false
                        }
                    }
                ) { Text("Save") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showRenameDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (scanResult == null) {
                // Initial State
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Scan Documents",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "High quality scans under 800KB",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
                SwissButton(
                    text = "Start Scanning",
                    onClick = {
                        val options = GmsDocumentScannerOptions.Builder()
                            .setGalleryImportAllowed(true)
                            .setPageLimit(100)
                            .setResultFormats(
                                GmsDocumentScannerOptions.RESULT_FORMAT_PDF,
                                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
                            )
                            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                            .build()

                        val scanner = GmsDocumentScanning.getClient(options)
                        scanner.getStartScanIntent(context as Activity)
                            .addOnSuccessListener { intentSender ->
                                scannerLauncher.launch(
                                    IntentSenderRequest.Builder(intentSender).build()
                                )
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to start scanner", Toast.LENGTH_SHORT).show()
                            }
                    }
                )
            } else {
                // Result State
                SuccessView(
                    pageCount = scanResult!!.pages?.size ?: 0,
                    pdfUri = currentPdfUri,
                    onReset = { 
                        scanResult = null
                        currentPdfUri = null
                    },
                    onRenameRequest = {
                        renameText = "Scanned_Doc_${System.currentTimeMillis()}"
                        showRenameDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun SuccessView(
    pageCount: Int,
    pdfUri: Uri?,
    onReset: () -> Unit,
    onRenameRequest: () -> Unit
) {
    val context = LocalContext.current
    
    SwissCard {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Scan Successful!",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "$pageCount pages scanned",
                style = MaterialTheme.typography.bodyMedium
            )
            
            if (pdfUri != null) {
                Text(
                    text = pdfUri.lastPathSegment ?: "scan.pdf",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                 SwissButton(
                     text = "Open",
                     onClick = {
                         pdfUri?.let { uri ->
                             val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                 setDataAndType(uri, "application/pdf")
                                 flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                             }
                             try { context.startActivity(intent) } catch(e:Exception)  { Toast.makeText(context, "No PDF Viewer", Toast.LENGTH_SHORT).show() }
                         }
                     },
                     modifier = Modifier.weight(1f)
                 )
                 SwissButton(
                     text = "Share",
                     onClick = {
                         pdfUri?.let { uri ->
                            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                 type = "application/pdf"
                                 putExtra(android.content.Intent.EXTRA_STREAM, uri)
                                 flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            context.startActivity(android.content.Intent.createChooser(intent, "Share PDF"))
                         }
                     },
                     modifier = Modifier.weight(1f),
                     primary = false
                 )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SwissButton(
                    text = "Rename", 
                    onClick = onRenameRequest,
                    primary = false,
                    modifier = Modifier.weight(1f)
                )
                SwissButton(
                    text = "New Scan", 
                    onClick = onReset,
                    primary = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
