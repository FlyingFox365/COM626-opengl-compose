package com.example.openglcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.openglcompose.ui.theme.OpenGLComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenGLComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OpenGLComposable(modifier = Modifier.padding(innerPadding),
                        floatArrayOf(0f,0f,0f))
                }
            }
        }
    }
}

// cameraPos will come from state and ViewModel via MutableStateFlow
@Composable
fun OpenGLComposable(modifier: Modifier, cameraPos: FloatArray) {
    AndroidView(modifier=modifier,
        factory = { ctx ->
           OpenGLView(ctx)
        },
        update = {
            // TODO
            // This should update the camera within the OpenGLView when the
            // parameter changes
        }
    )
}