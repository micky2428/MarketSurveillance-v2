@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketsurveillance

//https://github.com/philipplackner/CameraXGuide/blob/taking-photos/app/src/androidTest/java/com/plcoding/cameraxguide/ExampleInstrumentedTest.kt
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marketsurveillance.ui.theme.CameraXGuideTheme
import kotlinx.coroutines.launch


//class MainActivity : ComponentActivity()
class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0 //requestCode可以自己設定數字
            )
        }
        setContent {
            CameraXGuideTheme {
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberBottomSheetScaffoldState()
                val controller = remember { //camerapreview.kt
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases( //決定要有那些功能
                            CameraController.IMAGE_CAPTURE or
                                    CameraController.VIDEO_CAPTURE
                        )
                    }
                }
                val viewModel = viewModel<MainViewModel>()
                val bitmaps by viewModel.bitmaps.collectAsState()
                //Bottom Sheet 是一種從屏幕底部彈出的可互動的半透明面板，通常用於顯示補充信息、操作或菜單
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 0.dp,
                    sheetContent = {
                        PhotoBottomSheetContent(  //photobottomsheet.kt
                            bitmaps = bitmaps,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                ) { padding ->
                    Box(  //建立畫面邊框
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CameraPreview( //camerapreview.kt
                            controller = controller,
                            modifier = Modifier
                                .fillMaxSize()
                        )

                        IconButton(
                            onClick = {
                                controller.cameraSelector = //左上角前後鏡頭轉換
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else CameraSelector.DEFAULT_BACK_CAMERA
                            },
                            modifier = Modifier
                                .offset(16.dp, 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch camera"
                            )
                        }

                        Row( //下方選單
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton( //打開app內相簿
                                onClick = {
                                    scope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Photo,
                                    contentDescription = "Open gallery"
                                )
                            }
                            IconButton( //照相
                                onClick = {
                                    takePhoto(
                                        controller = controller,
                                        onPhotoTaken = viewModel::onTakePhoto
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Take photo"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
}


/**
 * @see "https://developer.android.com/training/camerax/architecture.combine-use-cases"
 */
//@RunWith(AndroidJUnit4::class)
//class CameraPreviewTest : LifecycleOwner, ImageReader.OnImageAvailableListener, Consumer<SurfaceRequest.Result> {
//
//    @get:Rule
//    val cameraAccess = GrantPermissionRule.grant(Manifest.permission.CAMERA)
//
//    private var registry: LifecycleRegistry? = null
//    private val thread = HandlerThread("CameraPreviewTest").also { it.start() }
//    private var executor = Executors.newSingleThreadExecutor()
//    private var provider: ProcessCameraProvider? = null // requires main thread
//
//    /**
//     * @implNote We can't use the main executor since it is reserved for the test framework.
//     */
//    @Before
//    fun setup() {
//        val context: Context = ApplicationProvider.getApplicationContext()
//        Assert.assertNotNull(context)
//        provider = ProcessCameraProvider.getInstance(context).get()
//        Assert.assertNotNull(provider)
//    }
//
//    @UiThreadTest
//    @After
//    fun teardown() {
//        provider?.unbindAll()
//        executor?.shutdown()
//    }
//
//    /**
//     * @implNote In checkPreviewUseCase, ImageReader will provide a Surface for camera preview test.
//     *  When each ImageProxy is acquired, the AtomicInteger will be incremented.
//     *  By doing so we can ensure the camera binding is working as expected.
//     */
//    private val reader = ImageReader.newInstance(1920, 1080, ImageFormat.YUV_420_888, 30)
//    private val count = AtomicInteger(0)
//
//    @Before
//    fun setupImageReader() {
//        reader.setOnImageAvailableListener(this, Handler(thread.looper))
//    }
//
//    @After
//    fun teardownImageReader() {
//        reader.close()
//        thread.quit()
//    }
//
//    override fun onImageAvailable(reader: ImageReader) {
//        reader.acquireNextImage().use { image ->
//            val imageNumber = count.getAndIncrement()
//            Log.i("CameraPreviewTest", String.format("image: %d %s", imageNumber, image))
//        }
//    }
//
//    /**
//     * @see ProcessCameraProvider.bindToLifecycle
//     */
//    override fun getLifecycle() = registry!!
//
//    @Before
//    fun markCreated() {
//        registry = LifecycleRegistry(this).also{
//            it.markState(Lifecycle.State.INITIALIZED)
//            it.markState(Lifecycle.State.CREATED)
//        }
//    }
//
//    @After
//    fun markDestroyed() {
//        registry?.markState(Lifecycle.State.DESTROYED)
//    }
//
//    /**
//     * @see SurfaceRequest.provideSurface
//     */
//    override fun accept(result: SurfaceRequest.Result) {
//        when (result.resultCode) {
//            SurfaceRequest.Result.RESULT_SURFACE_USED_SUCCESSFULLY -> {
//                Log.i("CameraPreviewTest", result.toString())
//            }
//            SurfaceRequest.Result.RESULT_REQUEST_CANCELLED, SurfaceRequest.Result.RESULT_INVALID_SURFACE, SurfaceRequest.Result.RESULT_SURFACE_ALREADY_PROVIDED, SurfaceRequest.Result.RESULT_WILL_NOT_PROVIDE_SURFACE -> {
//                Log.e("CameraPreviewTest", result.toString())
//            }
//        }
//    }
//
//
//    @UiThreadTest
//    @Test
//    fun checkPreviewUseCase() {
//        // life cycle owner
//        registry?.markState(Lifecycle.State.STARTED)
//
//        // select Back camera
//        val selectorBuilder = CameraSelector.Builder()
//        Assert.assertTrue(provider!!.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
//        selectorBuilder.requireLensFacing(CameraSelector.LENS_FACING_BACK)
//
//        // fit the preview size to ImageReader
//        val previewBuilder = Preview.Builder()
//        previewBuilder.setTargetResolution(Size(reader.width, reader.height))
//        previewBuilder.setTargetRotation(Surface.ROTATION_90)
//        val preview = previewBuilder.build()
//
//        // acquire camera binding
//        provider!!.unbindAll()
//        val camera = provider!!.bindToLifecycle(this, selectorBuilder.build(), preview)
//        Assert.assertNotNull(camera)
//        preview.setSurfaceProvider(executor!!, SurfaceProvider { request: SurfaceRequest ->
//            val surface = reader.surface
//            Log.i("CameraPreviewTest", String.format("providing: %s", surface))
//            request.provideSurface(surface, executor!!, this)
//        })
//
//        // wait until onImageAvailable is invoked. retry several times
//        for (repeat in 5 downTo 0) {
//            Thread.sleep(600)
//            val value = count.get()
//            Log.i("CameraPreviewTest", String.format("count: %d", value))
//            if (value > 0) return
//        }
//        Assert.assertNotEquals(0, count.get().toLong())
//    }
//}

