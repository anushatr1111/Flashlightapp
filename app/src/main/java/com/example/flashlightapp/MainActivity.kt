package com.example.flashlightapp

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashlightapp.ui.theme.FlashlightAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashlightAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FlashlightApp()
                }
            }
        }
    }
}

@Composable
fun FlashlightApp() {
    val context = LocalContext.current
    var isFlashlightOn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isFlashlightOn) "Flashlight is ON" else "Flashlight is OFF",
            style = MaterialTheme.typography.headlineMedium
        )
        Button(
            onClick = {
                isFlashlightOn = !isFlashlightOn
                toggleFlashlight(context, isFlashlightOn)
            }
        ) {
            Text(if (isFlashlightOn) "Turn OFF" else "Turn ON")
        }
    }
}

private fun toggleFlashlight(context: Context, enable: Boolean) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
        cameraId?.let {
            cameraManager.setTorchMode(it, enable)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun FlashlightAppPreview() {
    FlashlightAppTheme {
        FlashlightApp()
    }
}