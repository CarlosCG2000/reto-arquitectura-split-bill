package io.devexpert.splitbill.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import java.io.File
import io.devexpert.splitbill.R

// El Composable principal de la pantalla de inicio
@Composable
fun HomeScreen(
    viewmodel: HomeViewModel,
    onTicketProcessed: () -> Unit
) {
    val context = LocalContext.current
    val uiState = viewmodel.uiState.collectAsState()

    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para capturar foto con la cámara (alta resolución)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && photoUri != null) {
            val inputStream = context.contentResolver.openInputStream(photoUri!!)

            val bitmap = inputStream?.use { BitmapFactory.decodeStream(it) }

            if (bitmap != null) viewmodel.processTicket(bitmap)
      }
    }

    LaunchedEffect(uiState.value.ticketProcessed) {
        if (uiState.value.ticketProcessed) {
            onTicketProcessed()
            viewmodel.resetTicketProcessed()
        }
    }

    HomeScreenContent(
        uiState = uiState.value,
        onScanClicked = {
            val photoFile = File.createTempFile("ticket_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                "io.devexpert.splitbill.fileprovider",
                photoFile
            )
            photoUri = uri
            cameraLauncher.launch(uri)
        }
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onScanClicked: () -> Unit
) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (uiState.scansLeft > 0)
                        stringResource(R.string.scans_remaining, uiState.scansLeft)
                    else
                        stringResource(R.string.no_scans_remaining),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Button(
                    onClick = onScanClicked,
                    enabled = uiState.scansLeft > 0 && !uiState.isProcessing,
                    modifier = Modifier.size(width = 320.dp, height = 64.dp),
                    shape = ButtonDefaults.shape
                ) {
                    Text(
                        text = if (uiState.isProcessing)
                            stringResource(R.string.processing)
                        else
                            stringResource(R.string.scan_ticket),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                when {
                    uiState.isProcessing -> {
                        Text(
                            text = stringResource(R.string.photo_captured_processing),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                    uiState.errorMessage != null -> {
                        Text(
                            text = uiState.errorMessage,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}