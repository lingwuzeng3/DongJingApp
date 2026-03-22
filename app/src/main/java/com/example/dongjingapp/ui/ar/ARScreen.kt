package com.example.dongjingapp.ui.ar

import android.Manifest
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import java.nio.ByteBuffer
import java.util.concurrent.Executors

private const val TAG = "ARScreen"
private val mainHandler = Handler(Looper.getMainLooper())

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ARScreen() {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AR训练") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!cameraPermission.status.isGranted) {
                Text(
                    text = "需要相机权限以进行姿态检测",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
                return@Box
            }
            ARPoseCameraContent()
        }
    }
}

@Composable
private fun ARPoseCameraContent() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    var poseResult by remember { mutableStateOf<PoseLandmarkerResult?>(null) }
    var initError by remember { mutableStateOf<String?>(null) }
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }

    val landmarker = remember {
        try {
            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(
                    BaseOptions.builder()
                        .setModelAssetPath("pose_landmarker_lite.task")
                        .build()
                )
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setMinPoseDetectionConfidence(0.5f)
                .setMinPosePresenceConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setResultListener { result, _ ->
                    mainHandler.post { poseResult = result }
                }
                .setErrorListener { e ->
                    Log.e(TAG, "PoseLandmarker error", e)
                    mainHandler.post { initError = e.message }
                }
                .build()
            PoseLandmarker.createFromOptions(context, options)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create PoseLandmarker", e)
            initError = e.message ?: "初始化失败"
            null
        }
    }

    DisposableEffect(landmarker) {
        onDispose { landmarker?.close() }
    }

    DisposableEffect(lifecycleOwner, landmarker, previewViewRef) {
        val pv = previewViewRef
        val lm = landmarker
        if (pv == null || lm == null) {
            return@DisposableEffect onDispose { }
        }
        val future = ProcessCameraProvider.getInstance(context)
        val bindRunnable = Runnable {
            try {
                val cameraProvider = future.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = pv.surfaceProvider
                }
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                    .build()
                analysis.setAnalyzer(analysisExecutor) { imageProxy ->
                    processPoseFrame(imageProxy, lm)
                }
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    analysis
                )
            } catch (e: Exception) {
                Log.e(TAG, "bindToLifecycle failed", e)
                mainHandler.post { initError = e.message }
            }
        }
        future.addListener(bindRunnable, mainExecutor)
        onDispose {
            val unbind = Runnable {
                try {
                    if (future.isDone) {
                        future.get().unbindAll()
                    }
                } catch (_: Exception) {
                }
            }
            if (future.isDone) unbind.run()
            else future.addListener(unbind, mainExecutor)
            analysisExecutor.shutdown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            update = { previewViewRef = it },
            modifier = Modifier.fillMaxSize()
        )

        initError?.let { msg ->
            Text(
                text = "相机/姿态: $msg",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val result = poseResult ?: return@Canvas
            val poses = result.landmarks()
            if (poses.isEmpty()) return@Canvas
            val lm = poses[0]
            val w = size.width
            val h = size.height
            for (conn in PoseLandmarker.POSE_LANDMARKS) {
                val a = lm[conn.start()]
                val b = lm[conn.end()]
                drawLine(
                    color = Color(0xFF22C55E),
                    start = Offset(a.x() * w, a.y() * h),
                    end = Offset(b.x() * w, b.y() * h),
                    strokeWidth = 3f
                )
            }
            for (i in 0 until lm.size) {
                val p = lm[i]
                drawCircle(
                    color = Color(0xFFFFD700),
                    radius = 5f,
                    center = Offset(p.x() * w, p.y() * h),
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}

private fun processPoseFrame(imageProxy: ImageProxy, landmarker: PoseLandmarker) {
    try {
        val bitmap = rgbaImageProxyToBitmap(imageProxy) ?: return
        val mpImage = BitmapImageBuilder(bitmap).build()
        landmarker.detectAsync(mpImage, SystemClock.uptimeMillis())
    } catch (e: Exception) {
        Log.e(TAG, "Frame process error", e)
    } finally {
        imageProxy.close()
    }
}

private fun rgbaImageProxyToBitmap(image: ImageProxy): Bitmap? {
    if (image.planes.isEmpty()) return null
    val plane = image.planes[0]
    val buffer: ByteBuffer = plane.buffer.duplicate()
    buffer.rewind()
    val data = ByteArray(buffer.remaining())
    buffer.get(data)
    val bmp = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
    bmp.copyPixelsFromBuffer(ByteBuffer.wrap(data))
    return bmp
}
