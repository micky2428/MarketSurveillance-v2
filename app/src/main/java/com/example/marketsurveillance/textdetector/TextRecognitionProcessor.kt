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

//package com.google.mlkit.vision.demo.kotlin.textdetector

import android.content.Context
import android.util.Log
import com.example.marketsurveillance.textdetector.preference.PreferenceUtils
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface

//要解決這個問題，你需要在TextRecognitionProcessor類別中提供一個具體的processBitmap方法實現，以符合VisionProcessorBase抽象類別的要求。

/** Processor for the text detector demo. */
class TextRecognitionProcessor(
    private val context: Context,
    textRecognizerOptions: TextRecognizerOptionsInterface
) : VisionProcessorBase<Text>(context) {
    private val textRecognizer: TextRecognizer = TextRecognition.getClient(textRecognizerOptions)
    private val shouldGroupRecognizedTextInBlocks: Boolean =
        PreferenceUtils.shouldGroupRecognizedTextInBlocks(context)
    private val showLanguageTag: Boolean = PreferenceUtils.showLanguageTag(context)
    private val showConfidence: Boolean = PreferenceUtils.shouldShowTextConfidence(context)

    override fun stop() {
        super.stop()
        textRecognizer.close()
    }

    override fun detectInImage(image: InputImage): Task<Text> {
        return textRecognizer.process(image)
    }

    override fun onSuccess(text: Text, graphicOverlay: GraphicOverlay) {
        Log.d(TAG, "On-device Text detection successful")
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

//這段程式碼實現了一個文字辨識處理器 TextRecognitionProcessor，用於在圖像中檢測文字。它使用了 Google ML Kit 中的文字辨識功能。
// 讓我們來看一下主要的功能：
//TextRecognitionProcessor 接受一個 Context 和一個 TextRecognizerOptionsInterface 的實例作為構造函數的參數， 並繼承自 VisionProcessorBase<Text>。
//在構造函數中，它初始化了 textRecognizer，並根據應用程序中的偏好設置來設置一些參數，如是否將識別的文本分組顯示、是否顯示語言標籤、是否顯示文本置信度等。
//detectInImage 方法用於對圖像進行文字辨識，並返回一個 Task<Text> 對象。
//onSuccess 方法在文字辨識成功時被調用，它將檢測到的文字添加到圖形覆蓋層（GraphicOverlay）中，並在日誌中輸出一些額外的信息，以便進行測試。
//onFailure 方法在文字辨識失敗時被調用，它在日誌中輸出相應的錯誤信息。
//此外，還有一個名為 logExtrasForTesting 的輔助方法，用於在測試期間輸出一些額外的信息。