package com.example.marketsurveillance.textdetector

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


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.marketsurveillance.R
import com.example.marketsurveillance.textdetector.preference.PreferenceUtils
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface

//要解決這個問題，你需要在TextRecognitionProcessor類別中提供一個具體的processBitmap方法實現，以符合VisionProcessorBase抽象類別的要求。

//interface TextResultListener {
//    fun onTextResultDetected(resultText: String)
//}


/** Processor for the text detector demo. */
class TextRecognitionProcessor(  //連動stillimageactivity
    private val context: Context,
    textRecognizerOptions: TextRecognizerOptionsInterface,
) : VisionProcessorBase<Text>(context) {
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(textRecognizerOptions)
    private val shouldGroupRecognizedTextInBlocks: Boolean =
        PreferenceUtils.shouldGroupRecognizedTextInBlocks(context)
    private val showLanguageTag: Boolean = PreferenceUtils.showLanguageTag(context)
    private val showConfidence: Boolean = PreferenceUtils.shouldShowTextConfidence(context)

    //文字辨識輸出結果
    private var resultText: String = ""

//     定義一個函數用於處理辨識到的結果
//    private fun handleTextResult(resultText: String) {
//        // 在這裡處理辨識到的文字，例如將其顯示在文字框中
//        Log.d(TAG, "Recognized Text: $resultText")
//    }
//
//    // 将 TextResultListener 设置为属性
//    private var textResultListener: TextResultListener? = null
//
//    // 设置 TextResultListener
//    fun setTextResultListener(listener: TextResultListener) {
//        this.textResultListener = listener
//    }
//    }
//---
    override fun stop() {
        super.stop()
        textRecognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    //文字處理功能，按鈕@activitystill

    // 用于处理识别到的文字
    fun processText(text: Text) {
        val stringBuilder = StringBuilder()
        for (block in text.textBlocks) {
            for (line in block.lines) {
                stringBuilder.append(line.text).append("\n")
            }
        }

        // 将辨识到的文字设置为 resultText
        resultText = stringBuilder.toString()
    }

    // 启动下一个页面
//    fun startNextActivity() {
//        val intent = Intent(context, NextActivity::class.java).apply {
//            putExtra("resultText", resultText)
//        }
//        // 启动下一个页面
//        context.startActivity(intent)
//    }

//----

    override fun onSuccess(text: Text, graphicOverlay: GraphicOverlay) {
        Log.d(TAG, "On-device Text detection successful")
        //處理文字輸出
        processText(text)
        logExtrasForTesting(text)
        graphicOverlay.add(
            TextGraphic(
                graphicOverlay,
                text,
                shouldGroupRecognizedTextInBlocks,
                showLanguageTag,
                showConfidence
            )
        )

        Log.d(TAG, "Recognized Text: $resultText")
    }



    override fun onFailure(e: Exception) {
        Log.w(TAG, "Text detection failed.$e")
    }

    companion object {
        private const val TAG = "TextRecProcessor"
        private fun logExtrasForTesting(text: Text?) {
            if (text != null) {
                Log.v(MANUAL_TESTING_LOG, "Detected text has : " + text.textBlocks.size + " blocks")
                for (i in text.textBlocks.indices) {
                    val lines = text.textBlocks[i].lines
                    Log.v(
                        MANUAL_TESTING_LOG,
                        String.format("Detected text block %d has %d lines", i, lines.size)
                    )
                    for (j in lines.indices) {
                        val elements = lines[j].elements
                        Log.v(
                            MANUAL_TESTING_LOG,
                            String.format("Detected text line %d has %d elements", j, elements.size)
                        )
                        for (k in elements.indices) {
                            val element = elements[k]
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format("Detected text element %d says: %s", k, element.text)
                            )
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format(
                                    "Detected text element %d has a bounding box: %s",
                                    k,
                                    element.boundingBox!!.flattenToString()
                                )
                            )
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format(
                                    "Expected corner point size is 4, get %d",
                                    element.cornerPoints!!.size
                                )
                            )
                            for (point in element.cornerPoints!!) {
                                Log.v(
                                    MANUAL_TESTING_LOG,
                                    String.format(
                                        "Corner point for element %d is located at: x - %d, y = %d",
                                        k,
                                        point.x,
                                        point.y
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
class YourActivity : AppCompatActivity() {
    private lateinit var yourButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_still_image)

        // 找到按钮
        yourButton = findViewById(R.id.yourButton)

        // 为按钮设置点击监听器
        yourButton.setOnClickListener {
            // 在这里添加您的逻辑
            // 例如，启动下一个活动
            val intent = Intent(this, NextActivity::class.java)
            startActivity(intent)
        }
    }
}


//這段程式碼實現了一個文字辨識處理器 TextRecognitionProcessor，用於在圖像中檢測文字。它使用了 Google ML Kit 中的文字辨識功能。
// 讓我們來看一下主要的功能：
//TextRecognitionProcessor 接受一個 Context 和一個 TextRecognizerOptionsInterface 的實例作為構造函數的參數， 並繼承自 VisionProcessorBase<Text>。
//在構造函數中，它初始化了 textRecognizer，並根據應用程序中的偏好設置來設置一些參數，如是否將識別的文本分組顯示、是否顯示語言標籤、是否顯示文本置信度等。
//detectInImage 方法用於對圖像進行文字辨識，並返回一個 Task<Text> 對象。
//onSuccess 方法在文字辨識成功時被調用，它將檢測到的文字添加到圖形覆蓋層（GraphicOverlay）中，並在日誌中輸出一些額外的信息，以便進行測試。
//onFailure 方法在文字辨識失敗時被調用，它在日誌中輸出相應的錯誤信息。
//此外，還有一個名為 logExtrasForTesting 的輔助方法，用於在測試期間輸出一些額外的信息。

