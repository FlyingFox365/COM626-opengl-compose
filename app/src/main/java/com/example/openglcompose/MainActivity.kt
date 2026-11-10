package com.example.openglcompose

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.openglcompose.ui.theme.OpenGLComposeTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    val viewModel : OpenGLAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Set to true on the Lenovo tablets (no magnetometer) or false on devices with a magnetometer
        val useGameSensor = true
        setContent {

            val accelValues by viewModel.accelValues.collectAsState()
            val orientMtx by viewModel.orientMtx.collectAsState()

            OpenGLComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // TODO setup state variable for the orientation matrix

                    // TODO pass the current orientation matrix to OpenGLComposable
                    OpenGLComposable(
                        modifier = Modifier.padding(innerPadding),
                        orientMtx
                    )
                }
            }
        }
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val gameSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

        if(useGameSensor) {
            sensorManager.registerListener(
                this,
                gameSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        } else {
            sensorManager.registerListener(
                this,
                accelSensor,
                SensorManager.SENSOR_DELAY_UI
            )
            sensorManager.registerListener(
                this,
                magFieldSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onSensorChanged (ev: SensorEvent) {
        when(ev.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                viewModel.accelValues.value = ev.values.clone()
                val orientMtx = FloatArray(16)
                SensorManager.getRotationMatrix(
                    orientMtx,
                    null,
                    viewModel.accelValues.value,
                    viewModel.magFieldValues.value
                )
                viewModel.orientMtx.value = orientMtx.clone()
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                viewModel.magFieldValues.value = ev.values.clone()
            }

            Sensor.TYPE_GAME_ROTATION_VECTOR -> {
                val orientMtx = FloatArray(16)
                SensorManager.getRotationMatrixFromVector(orientMtx, ev.values)
                viewModel.orientMtx.value = orientMtx.clone()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, acc: Int) {

    }
}


@Composable
fun OpenGLComposable(modifier: Modifier, orientMtx: FloatArray) {
    AndroidView(modifier=modifier,
        factory = { ctx ->
           val glView = OpenGLView(ctx)
            glView
        },
        update = {
            gLView ->
            gLView.setOrientMtx(orientMtx)
        }
    )
}