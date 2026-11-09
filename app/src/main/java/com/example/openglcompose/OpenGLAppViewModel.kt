package com.example.openglcompose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class OpenGLAppViewModel : ViewModel() {

    val accelValues = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    val magFieldValues = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    val orientMtx = MutableStateFlow(FloatArray(16))

}