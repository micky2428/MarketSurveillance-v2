package com.example.marketsurveillance.textdetector.mode

/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.google.mlkit.vision.demo.kotlin
//這段程式碼是一個 Android 應用程式的活動 (Activity)，用於展示不同的圖像檢測功能。這個活動允許用戶從相機拍攝新照片或從現有圖片中選擇一張照片，然後對所選圖片進行各種圖像處理和檢測操作，如物件檢測、人臉檢測、條碼掃描、文本識別等。
//StillImageActivity：
//
//StillImageActivity 主要用于处理静态图像，用户可以选择从相机拍摄新照片或从现有照片中选择。
//用户可以选择不同的功能和机器学习模型应用于静态图像，如目标检测、人脸检测等。
//当用户选择不同的功能或模型时，会创建相应的图像处理器（imageProcessor），例如 ObjectDetectorProcessor 用于目标检测，FaceDetectorProcessor 用于人脸检测等。
//在 tryReloadAndDetectInImage() 方法中，会根据用户选择的功能和模型来处理加载的图像，并在 graphicOverlay 上显示检测结果。
//CameraXSourceDemoActivity：
//
//CameraXSourceDemoActivity 主要用于演示如何使用 CameraX 库的 CameraXSource 获取相机图像，并应用机器学习模型进行实时分析。
//相机图像不会直接显示在界面上，而是直接应用于图像处理器进行分析。相机图像不会自动保存到设备的相机卷中，而是在应用程序中进行处理和显示。
//这种方式更适合需要在应用程序中对相机图像进行实时处理、分析或其他操作的场景，而不是简单地保存图像到设备
//用户可以选择不同的功能和机器学习模型应用于相机图像，类似于 StillImageActivity。
//在 createImageProcessor() 方法中，会根据用户选择的功能和模型创建相应的图像处理器。
//不同之处在于，CameraXSourceDemoActivity 中直接使用 CameraX 库来处理相机图像，而不是通过静态图像加载和显示。
//StillImageActivity 和 CameraXSourceDemoActivity 的主要区别在于它们处理图像的方式以及图像的来源。

//import com.example.marketsurveillance.kotlin.barcodescanner.BarcodeScannerProcessor
//import com.example.marketsurveillance.kotlin.facedetector.FaceDetectorProcessor
//import com.example.marketsurveillance.kotlin.facemeshdetector.FaceMeshDetectorProcessor
//import com.example.marketsurveillance.kotlin.labeldetector.LabelDetectorProcessor
//import com.example.marketsurveillance.kotlin.objectdetector.ObjectDetectorProcessor
//import com.example.marketsurveillance.kotlin.posedetector.PoseDetectorProcessor
//import com.example.marketsurveillance.kotlin.segmenter.SegmenterProcessor
//import com.example.marketsurveillance.kotlin.subjectsegmenter.SubjectSegmenterProcessor
//import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
//import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
//import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions //印度文
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marketsurveillance.R
import com.example.marketsurveillance.textdetector.BitmapUtils
import com.example.marketsurveillance.textdetector.GraphicOverlay
import com.example.marketsurveillance.textdetector.TextRecognitionProcessor
import com.example.marketsurveillance.textdetector.VisionImageProcessor
import com.example.marketsurveillance.textdetector.preference.SettingsActivity
import com.example.marketsurveillance.textdetector.preference.SettingsActivity.LaunchSource
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

//從本地選擇圖像 (select_images_from_local)
//使用相機拍攝照片 (take_photo_using_camera)
/** Activity demonstrating different image detector features with a still image from camera. */
@KeepName
class StillImageActivity : AppCompatActivity() {
    private var preview: ImageView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedMode = OBJECT_DETECTION
    private var selectedSize: String? = SIZE_SCREEN
    private var isLandScape = false
    private var imageUri: Uri? = null
    // Max width (portrait mode)
    private var imageMaxWidth = 0
    // Max height (portrait mode)
    private var imageMaxHeight = 0
    private var imageProcessor: VisionImageProcessor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_still_image)
        findViewById<View>(R.id.select_image_button).setOnClickListener { view: View ->
            // Menu for selecting either: a) take new photo b) select from existing
            val popup = PopupMenu(this@StillImageActivity, view)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                val itemId = menuItem.itemId
                if (itemId == R.id.select_images_from_local) {
                    startChooseImageIntentForResult()
                    return@setOnMenuItemClickListener true
                } else if (itemId == R.id.take_photo_using_camera) {
                    startCameraIntentForResult()
                    return@setOnMenuItemClickListener true
                }
                false
            }
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.camera_button_menu, popup.menu)
            popup.show()
        }
        preview = findViewById(R.id.preview)
        graphicOverlay = findViewById(R.id.graphic_overlay)

        populateFeatureSelector()
        populateSizeSelector()
        isLandScape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(KEY_IMAGE_URI)
            imageMaxWidth = savedInstanceState.getInt(KEY_IMAGE_MAX_WIDTH)
            imageMaxHeight = savedInstanceState.getInt(KEY_IMAGE_MAX_HEIGHT)
            selectedSize = savedInstanceState.getString(KEY_SELECTED_SIZE)
        }

        val rootView = findViewById<View>(R.id.root)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    imageMaxWidth = rootView.width
                    imageMaxHeight = rootView.height - findViewById<View>(R.id.control).height
                    if (SIZE_SCREEN == selectedSize) {
                        tryReloadAndDetectInImage()
                    }
                }
            }
        )

        val settingsButton = findViewById<ImageView>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.STILL_IMAGE)
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createImageProcessor()
        tryReloadAndDetectInImage()
    }

    public override fun onPause() {
        super.onPause()
        imageProcessor?.run { this.stop() }
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run { this.stop() }
    }

    private fun populateFeatureSelector() {
        val featureSpinner = findViewById<Spinner>(R.id.feature_selector)
        val options: MutableList<String> = ArrayList()
//        options.add(OBJECT_DETECTION)
//        options.add(OBJECT_DETECTION_CUSTOM)
//        options.add(CUSTOM_AUTOML_OBJECT_DETECTION)
//        options.add(FACE_DETECTION)
//        options.add(BARCODE_SCANNING)
//        options.add(IMAGE_LABELING)
//        options.add(IMAGE_LABELING_CUSTOM)
//        options.add(CUSTOM_AUTOML_LABELING)
//        options.add(POSE_DETECTION)
//        options.add(SELFIE_SEGMENTATION)
        options.add(TEXT_RECOGNITION_LATIN)
        options.add(TEXT_RECOGNITION_CHINESE)
//        options.add(TEXT_RECOGNITION_DEVANAGARI)
        options.add(TEXT_RECOGNITION_JAPANESE)
        options.add(TEXT_RECOGNITION_KOREAN)
//        options.add(FACE_MESH_DETECTION)
        if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            options.add(SUBJECT_SEGMENTATION)
        }
//檢查 Android 系統的版本是否為 Android Nougat（API 級別 24）或更新版本。如果是的話，就將一個常量值 SUBJECT_SEGMENTATION 加入到 options 中。
//SUBJECT_SEGMENTATION 到 options 中，可能是為了指示某些操作或功能應該在支援主題分割的設備上執行。
// 主題分割是 Android Nougat 新增的一個功能，允許應用程式在分割的視窗中運行，以實現更好的多工體驗。

        // Creating adapter for featureSpinner
        val dataAdapter = ArrayAdapter(this, R.layout.spinner_style, options)
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        featureSpinner.adapter = dataAdapter
        featureSpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos >= 0) {
                        selectedMode = parentView.getItemAtPosition(pos).toString()
                        createImageProcessor()
                        tryReloadAndDetectInImage()
                    }
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
    }

    private fun populateSizeSelector() {
        val sizeSpinner = findViewById<Spinner>(R.id.size_selector)
        val options: MutableList<String> = ArrayList()
        options.add(SIZE_SCREEN)
        options.add(SIZE_1024_768)
        options.add(SIZE_640_480)
        options.add(SIZE_ORIGINAL)
        // Creating adapter for featureSpinner
        val dataAdapter = ArrayAdapter(this, R.layout.spinner_style, options)
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        sizeSpinner.adapter = dataAdapter
        sizeSpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos >= 0) {
                        selectedSize = parentView.getItemAtPosition(pos).toString()
                        tryReloadAndDetectInImage()
                    }
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_IMAGE_URI, imageUri)
        outState.putInt(KEY_IMAGE_MAX_WIDTH, imageMaxWidth)
        outState.putInt(KEY_IMAGE_MAX_HEIGHT, imageMaxHeight)
        outState.putString(KEY_SELECTED_SIZE, selectedSize)
    }

    private fun startCameraIntentForResult() { // Clean up last time's image
        imageUri = null
        preview!!.setImageBitmap(null)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun startChooseImageIntentForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CHOOSE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            tryReloadAndDetectInImage()
        } else if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            // In this case, imageUri is returned by the chooser, save it.
            imageUri = data!!.data
            tryReloadAndDetectInImage()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun tryReloadAndDetectInImage() {
        Log.d(TAG, "Try reload and detect image")
        try {
            if (imageUri == null) {
                return
            }

            if (SIZE_SCREEN == selectedSize && imageMaxWidth == 0) {
                // UI layout has not finished yet, will reload once it's ready.
                return
            }

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(contentResolver, imageUri!!) ?: return
            // Clear the overlay first
            graphicOverlay!!.clear()

            val resizedBitmap: Bitmap
            resizedBitmap =
                if (selectedSize == SIZE_ORIGINAL) {
                    imageBitmap
                } else {
                    // Get the dimensions of the image view
                    val targetedSize: Pair<Int, Int> = targetedWidthHeight

                    // Determine how much to scale down the image
                    val scaleFactor =
                        Math.max(
                            imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
                            imageBitmap.height.toFloat() / targetedSize.second.toFloat()
                        )
                    Bitmap.createScaledBitmap(
                        imageBitmap,
                        (imageBitmap.width / scaleFactor).toInt(),
                        (imageBitmap.height / scaleFactor).toInt(),
                        true
                    )
                }

            preview!!.setImageBitmap(resizedBitmap)
            if (imageProcessor != null) {
                graphicOverlay!!.setImageSourceInfo(
                    resizedBitmap.width,
                    resizedBitmap.height,
                    /* isFlipped= */ false
                )
                imageProcessor!!.processBitmap(resizedBitmap, graphicOverlay)
            } else {
                Log.e(TAG, "Null imageProcessor, please check adb logs for imageProcessor creation error")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error retrieving saved image")
            imageUri = null
        }
    }
    //文字识别的处理部分由 tryReloadAndDetectInImage() 函数负责。在这个函数中，识别出的文字被处理后会通过 GraphicOverlay 显示在界面上。

    private val targetedWidthHeight: Pair<Int, Int>
        get() {
            val targetWidth: Int
            val targetHeight: Int
            when (selectedSize) {
                SIZE_SCREEN -> {
                    targetWidth = imageMaxWidth
                    targetHeight = imageMaxHeight
                }
                SIZE_640_480 -> {
                    targetWidth = if (isLandScape) 640 else 480
                    targetHeight = if (isLandScape) 480 else 640
                }
                SIZE_1024_768 -> {
                    targetWidth = if (isLandScape) 1024 else 768
                    targetHeight = if (isLandScape) 768 else 1024
                }
                else -> throw IllegalStateException("Unknown size")
            }
            return Pair(targetWidth, targetHeight)
        }

    private fun createImageProcessor() {
        try {
            when (selectedMode) {
//                OBJECT_DETECTION -> {
//                    Log.i(TAG, "Using Object Detector Processor")
//                    val objectDetectorOptions = PreferenceUtils.getObjectDetectorOptionsForStillImage(this)
//                    imageProcessor = ObjectDetectorProcessor(this, objectDetectorOptions)
//                }
//                OBJECT_DETECTION_CUSTOM -> {
//                    Log.i(TAG, "Using Custom Object Detector Processor")
//                    val localModel =
//                        LocalModel.Builder().setAssetFilePath("custom_models/object_labeler.tflite").build()
//                    val customObjectDetectorOptions =
//                        PreferenceUtils.getCustomObjectDetectorOptionsForStillImage(this, localModel)
//                    imageProcessor = ObjectDetectorProcessor(this, customObjectDetectorOptions)
//                }
//                CUSTOM_AUTOML_OBJECT_DETECTION -> {
//                    Log.i(TAG, "Using Custom AutoML Object Detector Processor")
//                    val customAutoMLODTLocalModel =
//                        LocalModel.Builder().setAssetManifestFilePath("automl/manifest.json").build()
//                    val customAutoMLODTOptions =
//                        PreferenceUtils.getCustomObjectDetectorOptionsForStillImage(
//                            this,
//                            customAutoMLODTLocalModel
//                        )
//                    imageProcessor = ObjectDetectorProcessor(this, customAutoMLODTOptions)
//                }
//                FACE_DETECTION -> {
//                    Log.i(TAG, "Using Face Detector Processor")
//                    val faceDetectorOptions = PreferenceUtils.getFaceDetectorOptions(this)
//                    imageProcessor = FaceDetectorProcessor(this, faceDetectorOptions)
//                }
//                BARCODE_SCANNING -> imageProcessor = BarcodeScannerProcessor(this, zoomCallback = null)
                TEXT_RECOGNITION_LATIN ->
                    imageProcessor = TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build())
                TEXT_RECOGNITION_CHINESE ->
                    imageProcessor =
                        TextRecognitionProcessor(this, ChineseTextRecognizerOptions.Builder().build())
//                TEXT_RECOGNITION_DEVANAGARI ->
//                    imageProcessor =
//                        TextRecognitionProcessor(this, DevanagariTextRecognizerOptions.Builder().build())
                TEXT_RECOGNITION_JAPANESE ->
                    imageProcessor =
                        TextRecognitionProcessor(this, JapaneseTextRecognizerOptions.Builder().build())
                TEXT_RECOGNITION_KOREAN ->
                    imageProcessor =
                        TextRecognitionProcessor(this, KoreanTextRecognizerOptions.Builder().build())
//                IMAGE_LABELING ->
//                    imageProcessor = LabelDetectorProcessor(this, ImageLabelerOptions.DEFAULT_OPTIONS)
//                IMAGE_LABELING_CUSTOM -> {
//                    Log.i(TAG, "Using Custom Image Label Detector Processor")
//                    val localClassifier =
//                        LocalModel.Builder().setAssetFilePath("custom_models/bird_classifier.tflite").build()
//                    val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localClassifier).build()
//                    imageProcessor = LabelDetectorProcessor(this, customImageLabelerOptions)
//                }
//                CUSTOM_AUTOML_LABELING -> {
//                    Log.i(TAG, "Using Custom AutoML Image Label Detector Processor")
//                    val customAutoMLLabelLocalModel =
//                        LocalModel.Builder().setAssetManifestFilePath("automl/manifest.json").build()
//                    val customAutoMLLabelOptions =
//                        CustomImageLabelerOptions.Builder(customAutoMLLabelLocalModel)
//                            .setConfidenceThreshold(0f)
//                            .build()
//                    imageProcessor = LabelDetectorProcessor(this, customAutoMLLabelOptions)
//                }
//                POSE_DETECTION -> {
//                    val poseDetectorOptions = PreferenceUtils.getPoseDetectorOptionsForStillImage(this)
//                    Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
//                    val shouldShowInFrameLikelihood =
//                        PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodStillImage(this)
//                    val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
//                    val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
//                    val runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this)
//                    imageProcessor =
//                        PoseDetectorProcessor(
//                            this,
//                            poseDetectorOptions,
//                            shouldShowInFrameLikelihood,
//                            visualizeZ,
//                            rescaleZ,
//                            runClassification,
//                            isStreamMode = false
//                        )
//                }
//                SELFIE_SEGMENTATION -> {
//                    imageProcessor = SegmenterProcessor(this, isStreamMode = false)
//                }
//                FACE_MESH_DETECTION -> {
//                    imageProcessor = FaceMeshDetectorProcessor(this)
//                }
//                SUBJECT_SEGMENTATION -> {
//                    if (VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        imageProcessor = SubjectSegmenterProcessor(this)
//                    }
//                }
                else -> Log.e(TAG, "Unknown selectedMode: $selectedMode")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can not create image processor: $selectedMode", e)
            Toast.makeText(
                applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    companion object {
        private const val TAG = "StillImageActivity"
        private const val OBJECT_DETECTION = "Object Detection"
//        private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"
//        private const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
//        private const val FACE_DETECTION = "Face Detection"
//        private const val BARCODE_SCANNING = "Barcode Scanning"
        private const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin(拉丁文)"
        private const val TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese(中文)"
//        private const val TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari"
        private const val TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese(日文)"
        private const val TEXT_RECOGNITION_KOREAN = "Text Recognition Korean(韓文)"
//        private const val IMAGE_LABELING = "Image Labeling"
//        private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
//        private const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
//        private const val POSE_DETECTION = "Pose Detection"
//        private const val SELFIE_SEGMENTATION = "Selfie Segmentation"
//        private const val FACE_MESH_DETECTION = "Face Mesh Detection (Beta)"
        private const val SUBJECT_SEGMENTATION = "Subject Segmentation (Beta)"

        private const val SIZE_SCREEN = "w:screen" // Match screen width
        private const val SIZE_1024_768 = "w:1024" // ~1024*768 in a normal ratio
        private const val SIZE_640_480 = "w:640" // ~640*480 in a normal ratio
        private const val SIZE_ORIGINAL = "w:original" // Original image size
        private const val KEY_IMAGE_URI = "com.example.marketsurveillance.textdetector.KEY_IMAGE_URI"
        private const val KEY_IMAGE_MAX_WIDTH = "com.example.marketsurveillance.textdetector.KEY_IMAGE_MAX_WIDTH"
        private const val KEY_IMAGE_MAX_HEIGHT = "com.example.marketsurveillance.textdetector.KEY_IMAGE_MAX_HEIGHT"
        private const val KEY_SELECTED_SIZE = "com.example.marketsurveillance.textdetector.KEY_SELECTED_SIZE"
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val REQUEST_CHOOSE_IMAGE = 1002
    }
}