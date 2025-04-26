package com.androminor.mangaapp.presentation.face

import BottomNavController
import FaceDetectionViewModel
import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

// ImageProxy extension functions remain unchanged
fun ImageProxy.manualToBitmap(): Bitmap {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)
    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
    return BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size())
}

fun ImageProxy.toBitmapWithRotation(isFrontCamera: Boolean): Bitmap {
    val bitmap = this.manualToBitmap()

    val matrix = Matrix().apply {
        postRotate(imageInfo.rotationDegrees.toFloat())
        if (isFrontCamera) {
            postScale(-1f, 1f) // Horizontal mirror for front camera
        }
    }

    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceScreen(
    viewModel: FaceDetectionViewModel = hiltViewModel(),
    selectedTab: Int,
    navController: NavController,
    onTabSelected: (Int)->Unit
) {
    val hasCameraPermission by viewModel.hasCameraPermission.collectAsState()
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    LaunchedEffect(cameraPermissionState.status) {
        viewModel.updateCameraPermission(cameraPermissionState.status.isGranted)
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            // Custom bottom navigation bar matching the design from screenshots
            /* Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF212121))
                    .height(60.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        // Manga Tab
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(36.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Transparent)
                                .clickable { onBackToHome() }
                                .padding(horizontal = 24.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.baseline_menu_book_24),
                                contentDescription = "Manga",
                                tint = if (selectedTab == 0) Color.Gray else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        // Small space between icon and text
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Manga",
                            color = if(selectedTab==0)Color.White else Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }


                    // Face Tab (selected)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Face",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }*/
            BottomNavController(
                navController = navController,
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            if (hasCameraPermission) {
                FaceDetectionContent()
            } else {
                CameraPermissionRequest(cameraPermissionState)
            }
        }
    }
}

// CameraPermissionRequest and FaceDetectionContent remain unchanged
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionRequest(cameraPermissionState: PermissionState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Camera Permission Required",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This feature needs camera access to detect faces. Please grant permission to use your camera.",
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { cameraPermissionState.launchPermissionRequest() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            )
        ) {
            Text("Grant Permission")
        }
    }
}

@Composable
fun FaceDetectionContent() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var faceDetected by remember { mutableStateOf(false) }
    var faceRect by remember { mutableStateOf<Rect?>(null) }
    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Store image dimensions for proper scaling
    var imageWidth by remember { mutableStateOf(0) }
    var imageHeight by remember { mutableStateOf(0) }

    DisposableEffect(key1 = Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        // Store lens facing information
        val isFrontCamera = true // Since we're explicitly using front camera

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            try {
                Log.d("FaceDetection", "Processing frame: ${imageProxy.width}x${imageProxy.height}")
                imageWidth = imageProxy.width
                imageHeight = imageProxy.height

                // Load the model differently - try to use assets file descriptor
                val modelPath = "face_detection_short_range.tflite"
                Log.d("FaceDetection", "Loading model from: $modelPath")

                val baseOptions = try {
                    // Copy the model file from assets to cache directory
                    val modelFile = File(context.cacheDir, modelPath)
                    if (!modelFile.exists()) {
                        context.assets.open(modelPath).use { input ->
                            FileOutputStream(modelFile).use { output ->
                                input.copyTo(output)
                            }
                        }
                    }

                    // Use the file path directly
                    BaseOptions.builder()
                        .setModelAssetPath(modelFile.absolutePath)
                        .build()
                } catch (e: Exception) {
                    Log.e(
                        "FaceDetection",
                        "Failed to copy model to cache, fallback to asset path",
                        e
                    )
                    BaseOptions.builder()
                        .setModelAssetPath(modelPath)
                        .build()
                }

                val options = FaceDetector.FaceDetectorOptions.builder()
                    .setBaseOptions(baseOptions as BaseOptions?)
                    .setMinDetectionConfidence(0.3f) // Lower threshold for better detection
                    .build()

                val faceDetector = FaceDetector.createFromOptions(context, options)

                // Use the extension function to get bitmap with proper rotation
                val bitmap = imageProxy.manualToBitmap()
                Log.d("FaceDetection", "Bitmap created: ${bitmap.width}x${bitmap.height}")

                // Create MediaPipe image
                val mpImage = BitmapImageBuilder(bitmap).build()

                // Process the image
                val result = faceDetector.detect(mpImage)
                Log.d(
                    "FaceDetection",
                    "Detection complete: ${result.detections().size} faces found"
                )

                // Update UI state
                if (result.detections().isNotEmpty()) {
                    val face = result.detections().first()
                    faceDetected = true

                    val score = face.categories().firstOrNull()?.score() ?: 0f
                    Log.d("FaceDetection", "Face detected with confidence: $score")

                    // For front camera, we need to mirror the coordinates
                    val boundingBox = face.boundingBox()
                    Log.d("FaceDetection", "Original bounding box: $boundingBox")

                    val mirroredRect = if (isFrontCamera) {
                        RectF(
                            bitmap.width - boundingBox.right,
                            boundingBox.top,
                            bitmap.width - boundingBox.left,
                            boundingBox.bottom
                        )
                    } else {
                        boundingBox
                    }
                    Log.d("FaceDetection", "Mirrored bounding box: $mirroredRect")

                    faceRect = Rect(
                        mirroredRect.left.toInt(),
                        mirroredRect.top.toInt(),
                        mirroredRect.right.toInt(),
                        mirroredRect.bottom.toInt()
                    )
                } else {
                    faceDetected = false
                    faceRect = null
                    Log.d("FaceDetection", "No face detected")
                }

                // Close the face detector
                faceDetector.close()
            } catch (e: Exception) {
                Log.e("FaceDetection", "Error processing image", e)
                e.printStackTrace() // Full stack trace
                faceDetected = false
                faceRect = null
            } finally {
                imageProxy.close()
            }
        }

        try {
            cameraProvider.unbindAll()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )

            Log.d("FaceDetection", "Camera setup complete")
        } catch (e: Exception) {
            Log.e("FaceDetection", "Camera binding failed", e)
        }

        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { previewView }
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Reference rectangle - this is the green guidance rectangle
                val rectWidth = canvasWidth * 0.7f
                val rectHeight = canvasHeight * 0.4f
                val rectLeft = (canvasWidth - rectWidth) / 2
                val rectTop = (canvasHeight - rectHeight) / 2

                drawRect(
                    color = Color.Green,
                    topLeft = Offset(rectLeft, rectTop),
                    size = Size(rectWidth, rectHeight),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Detected face rectangle - this is the red rectangle
                faceRect?.let { rect ->
                    if (imageWidth > 0 && imageHeight > 0) {
                        // Proper scaling factors based on actual image dimensions
                        val scaleX = canvasWidth / imageWidth.toFloat()
                        val scaleY = canvasHeight / imageHeight.toFloat()

                        drawRect(
                            color = Color.Red,
                            topLeft = Offset(rect.left * scaleX, rect.top * scaleY),
                            size = Size(rect.width() * scaleX, rect.height() * scaleY),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }

            Text(
                text = if (faceDetected) "Face Detected" else "No Face Detected",
                color = if (faceDetected) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp)
            )
        }
    }
}