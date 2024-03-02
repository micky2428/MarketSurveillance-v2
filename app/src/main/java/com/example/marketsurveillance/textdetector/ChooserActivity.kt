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
//這段程式碼是選擇實時預覽應用程式(如LivePreviewActivity,StillImage,CameraXpreview,CameraXsource)的活動，其主要功能包括：
//
//初始化相機預覽和圖形覆蓋層。
//設置下拉選單，用戶可以從中選擇要應用的機器學習模型。
//設置切換按鈕，用戶可以切換相機的前後置鏡頭。
//提供設置按鈕，用戶可以設置應用的偏好設置。
//根據用戶選擇的模型創建相應的相機源和機器學習處理器。
//啟動或重新啟動相機源。
//在活動的生命周期方法中處理相機的啟動、暫停和銷毀。
//此外，這段程式碼還包括了一些常量，這些常量表示可用的機器學習模型，如物件檢測、人臉檢測、文本識別等。這些常量用於初始化下拉選單，讓用戶可以從中選擇不同的模型。
//package com.google.mlkit.vision.demo.kotlin
//它創建了一個列表（ListView）來顯示各種測試活動的名稱，當用戶點擊列表項目時，它會啟動相應的測試活動。
// 該列表使用了自定義的 MyArrayAdapter 類來管理數據和顯示，該適配器可以設置列表項目的描述。

import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.marketsurveillance.R
import com.example.marketsurveillance.textdetector.mode.StillImageActivity

/** Demo app chooser which allows you pick from all available testing Activities. */
class ChooserActivity :
    AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback, OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_chooser)

        // Set up ListView and Adapter
        val listView = findViewById<ListView>(R.id.test_activity_list_view)
        val adapter = MyArrayAdapter(this, android.R.layout.simple_list_item_2, CLASSES)
        adapter.setDescriptionIds(DESCRIPTION_IDS)
        listView.adapter = adapter
        listView.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val clicked = CLASSES[position]
        startActivity(Intent(this, clicked))
    }

    private class MyArrayAdapter(
        private val ctx: Context,
        resource: Int,
        private val classes: Array<Class<*>>
    ) : ArrayAdapter<Class<*>>(ctx, resource, classes) {
        private var descriptionIds: IntArray? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView

            if (convertView == null) {
                val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(android.R.layout.simple_list_item_2, null)
            }

            (view!!.findViewById<View>(android.R.id.text1) as TextView).text =
                classes[position].simpleName
            descriptionIds?.let {
                (view.findViewById<View>(android.R.id.text2) as TextView).setText(it[position])
            }

            return view
        }

        fun setDescriptionIds(descriptionIds: IntArray) {
            this.descriptionIds = descriptionIds
        }
    }

    companion object {
        private const val TAG = "ChooserActivity"
        private val CLASSES =
            if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP)
                arrayOf<Class<*>>(
//                    LivePreviewActivity::class.java,
                    StillImageActivity::class.java,
                )
            else
                arrayOf<Class<*>>( //可選的四種模式
//                    LivePreviewActivity::class.java,
                    StillImageActivity::class.java,
//                    CameraXLivePreviewActivity::class.java,
//                    CameraXSourceDemoActivity::class.java
                )
        private val DESCRIPTION_IDS = //待確定後，文字也要改
            if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP)
                intArrayOf(
                    R.string.desc_camera_source_activity,
                    R.string.desc_still_image_activity,
                )
            else
                intArrayOf(
                    R.string.desc_camera_source_activity,
                    R.string.desc_still_image_activity,
                    R.string.desc_camerax_live_preview_activity,
                    R.string.desc_cameraxsource_demo_activity
                )
    }
}

////
//這段程式碼創建了一個名為 DESCRIPTION_IDS 的整數數組。根據 Android 系統的版本，它包含不同活動的描述資源的 ID。這些資源 ID 用於顯示在測試活動列表的第二行，提供對每個測試活動的簡短描述。
//具體來說，如果 Android 系統的版本小於 LOLLIPOP（即 Android 5.0），則只有兩個活動的描述資源 ID 被包含在 DESCRIPTION_IDS 數組中，分別是 R.string.desc_camera_source_activity
//和 R.string.desc_still_image_activity。否則，如果 Android 系統的版本是 LOLLIPOP 或更高版本，則還會包含其他兩個活動的描述資源 ID，分別是 R.string.desc_camerax_live_preview_activity 和 R.string.desc_cameraxsource_demo_activity。