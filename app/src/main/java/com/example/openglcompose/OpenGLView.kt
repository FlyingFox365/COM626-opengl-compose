package com.example.openglcompose

import android.opengl.GLSurfaceView
import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import android.opengl.GLES20
import android.util.Log
import javax.microedition.khronos.opengles.GL10
import freemap.openglwrapper.GPUInterface
import freemap.openglwrapper.OpenGLUtils
import java.io.IOException

class OpenGLView(val ctx: Context)  :GLSurfaceView(ctx), GLSurfaceView.Renderer {
    init {
        setEGLContextClientVersion(2) // specify OpenGL ES 2.0
        setRenderer(this) // set the renderer for this GLSurfaceView
    }

    lateinit var gpu: GPUInterface
    var refUColour = -1

    // We initialise the rendering here
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background colour (red=0, green=0, blue=0, alpha=1)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Enable depth testing - will cause nearer 3D objects to automatically
        // be drawn over further objects
        GLES20.glClearDepthf(1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        gpu = GPUInterface("mainGpu")
        try {
            if(!gpu.loadShaders(ctx.assets, "vertex.glsl", "fragment.glsl")) {
                Log.e("OpenGL01", gpu.lastShaderError)
            } else {
                val coords = floatArrayOf (
                    0f, 0f, 0f,
                    1f, 0f, 0f,
                    0f, 1f, 0f,
                        )
                val fbuf = OpenGLUtils.makeFloatBuffer(coords)

                val refAVertex = gpu.getAttribLocation("aVertex")
                gpu.specifyBufferedDataFormat(refAVertex, fbuf, 0)
                refUColour = gpu.getUniformLocation("uColour")
               
            }
        } catch (e: IOException) {
            Log.e("OpenGL01", e.stackTraceToString())
        }
    }

    // We draw our shapes here
    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        if(refUColour >= 0) {

            val red = floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)
            gpu.setUniform4FloatArray(refUColour, red)
            gpu.drawBufferedTriangles(0, 3)

        }
    }

    // Used if the screen is resized
    override fun onSurfaceChanged(unused: GL10, w: Int, h: Int) {
        GLES20.glViewport(0, 0, w, h)
    }
}