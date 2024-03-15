package com.project.spass.presentation.screens.admin_screen.component

// Open Camera to scan the QR code

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.spass.presentation.graphs.detail_graph.DetailScreen
import com.project.spass.presentation.graphs.home_graph.ShopHomeScreen
import com.project.spass.presentation.ui.theme.SPass

@Composable
fun AdminScreen(navController: NavHostController) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == 0) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data")
        }
    }
    var qrCode by remember {
        mutableStateOf("")
    }
    var qrCodeError by remember {
        mutableStateOf(false)
    }
    var qrCodeErrorText by remember {
        mutableStateOf("")
    }
    var qrCodeSuccess by remember {
        mutableStateOf(false)
    }
    var qrCodeSuccessText by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Admin Screen",
            style = MaterialTheme.typography.h5,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = qrCode,
            onValueChange = {
                qrCode = it
                qrCodeError = false
                qrCodeErrorText = ""
                qrCodeSuccess = false
                qrCodeSuccessText = ""
            },
            label = { Text("QR Code") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = qrCodeError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (qrCode.isEmpty()) {
                    qrCodeError = true
                    qrCodeErrorText = "QR Code is required"
                } else {
                    qrCodeError = false
                    qrCodeErrorText = ""
                    qrCodeSuccess = true
                    qrCodeSuccessText = "QR Code scanned successfully"
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    launcher.launch(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Scan QR Code")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (qrCodeError) {
            Text(
                text = qrCodeErrorText,
                style = MaterialTheme.typography.body2,
                color = Color.Red
            )
        }
        if (qrCodeSuccess) {
            Text(
                text = qrCodeSuccessText,
                style = MaterialTheme.typography.body2,
                color = Color.Green
            )
        }

    }

}
