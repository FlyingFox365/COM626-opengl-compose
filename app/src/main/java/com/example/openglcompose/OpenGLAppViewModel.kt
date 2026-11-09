package com.example.openglcompose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class OpenGLAppViewModel : ViewModel() {
    val translationChange = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    val rotationYChange = MutableStateFlow(0f)
}