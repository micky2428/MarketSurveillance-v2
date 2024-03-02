package com.example.marketsurveillance.textdetector.preference

import android.os.Bundle
import android.preference.PreferenceFragment
import androidx.appcompat.app.AppCompatActivity
import com.example.marketsurveillance.R
import com.example.marketsurveillance.textdetector.preference.SettingsActivity.LaunchSource

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
//package com.google.mlkit.vision.demo.preference
//import com.google.mlkit.vision.demo.R

/**
 * Hosts the preference fragment to configure settings for a demo activity that specified by the
 * [LaunchSource].
 */
class SettingsActivity : AppCompatActivity() {
    /** Specifies where this activity is launched from.  */
    // CameraX is only available on API 21+
    enum class LaunchSource(
        val titleResId: Int,
        val prefFragmentClass: Class<out PreferenceFragment?>
    ) {
//        LIVE_PREVIEW(
//            R.string.pref_screen_title_live_preview,
//            LivePreviewPreferenceFragment::class.java
//        ),
        STILL_IMAGE(
            R.string.pref_screen_title_still_image,
            StillImagePreferenceFragment::class.java
        ),
//        CAMERAX_LIVE_PREVIEW(
//            R.string.pref_screen_title_camerax_live_preview,
//            CameraXLivePreviewPreferenceFragment::class.java
//        ),
//        CAMERAXSOURCE_DEMO(
//            R.string.pref_screen_title_cameraxsource_demo,
//            CameraXSourceDemoPreferenceFragment::class.java
//        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val launchSource: com.example.marketsurveillance.textdetector.preference.SettingsActivity.LaunchSource? =
            intent.getSerializableExtra(
                EXTRA_LAUNCH_SOURCE
            ) as com.example.marketsurveillance.textdetector.preference.SettingsActivity.LaunchSource?
        val actionBar = supportActionBar
        if (launchSource != null) {
            actionBar?.setTitle(launchSource.titleResId)
        }
        try {
            if (launchSource != null) {
                fragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.settings_container,
                        launchSource.prefFragmentClass.getDeclaredConstructor().newInstance()
                    )
                    .commit()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    companion object {
        const val EXTRA_LAUNCH_SOURCE = "extra_launch_source"
    }
}

//这段代码是一个设置界面的 Activity，通过指定 LaunchSource 来确定其功能。在这个 SettingsActivity 中，不同的 LaunchSource 对应不同的设置选项，如拍照、实时预览等。
//
//具体来说：
//
//LaunchSource 是一个枚举类型，它指定了不同设置选项的标题和对应的 PreferenceFragment 类。
//
//在 onCreate 方法中，首先获取从 Intent 中传递过来的 EXTRA_LAUNCH_SOURCE 参数，该参数表示了启动该设置界面的来源。
//
//然后根据 EXTRA_LAUNCH_SOURCE 参数确定了界面的标题，并将对应的 PreferenceFragment 替换到 settings_container 布局容器中。