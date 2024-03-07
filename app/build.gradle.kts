plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //kapt-kotlin插件
    kotlin("kapt")
    //hilt-https://developer.android.com/training/dependency-injection/hilt-android?hl=zh-tw
    //google login
    //project那邊也要新增，否則會錯誤
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.marketsurveillance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.marketsurveillance"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
//        viewBinding = true
//        dataBinding = true 未解決的問題
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/{DEPENDENCIES}"

            excludes +="META-INF/LICENSE"
            excludes +="META-INF/LICENSE.txt"
            excludes +="META-INF/license.txt"
            excludes +="META-INF/NOTICE"
            excludes +="META-INF/NOTICE.txt"
            excludes +="META-INF/notice.txt"
            excludes +="META-INF/ASL2.0"
            excludes +="META-INF/*.kotlin_module"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-inappmessaging:20.4.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    //productInfo頁面
    implementation ("androidx.compose.foundation:foundation-layout:<version>")
    implementation ("androidx.compose.material:material:1.6.1")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.ui:ui:1.6.1")
    //photoupload頁面
    implementation ("androidx.appcompat:appcompat:1.6.1")
//    //google權限
    implementation ("com.google.android.gms:play-services-auth:21.0.0")
    implementation ("org.eclipse.jetty:jetty-server:11.0.6")
    implementation ("com.google.oauth-client:google-oauth-client-java6:1.31.1")
    implementation ("com.google.android.gms:play-services-drive:17.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")
    implementation ("com.google.http-client:google-http-client-gson:1.42.3")
    implementation ("com.google.apis:google-api-services-docs:v1-rev20230929-2.0.0")
    implementation ("com.google.guava:guava:24.1-jre")
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
//    implementation("com.google.apis:google-api-services-drive:v3-rev136-1.25.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("com.android.volley:volley:1.2.1")
//google drive api
    implementation ("com.google.api-client:google-api-client:2.0.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation ("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    //從firebase認證google(據說比較容易成功,方便管理數據)
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth")
    //google相關
    implementation ("com.google.api-client:google-api-client-android:1.32.1")
    implementation ("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    //這個插件是 Coil Compose，它是一個針對 Jetpack Compose 框架設計的圖像加載庫。它可以讓你在 Compose 應用程序中輕鬆地加載圖像
    implementation("io.coil-kt:coil-compose:2.5.0")
//最新認證方式
    implementation("androidx.credentials:credentials:1.3.0-alpha01")
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
//1130216
    implementation ("com.fasterxml.jackson.core:jackson-core:2.12.5") // 根據您的需求選擇版本
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.12.5") // 根據您的需求選擇版本
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.12.5")
    implementation ("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
//相機
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.09.00"))
    implementation("androidx.compose.material:material-icons-extended:1.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    val camerax_version = "1.3.0-rc01"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")
    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")
    //便是
    //https://developers.google.com/ml-kit/vision/text-recognition/v2/android?hl=zh-tw
    implementation ("com.google.mlkit:text-recognition:16.0.0")

    // To recognize Chinese script
    implementation ("com.google.mlkit:text-recognition-chinese:16.0.0")

    // To recognize Devanagari script
    implementation ("com.google.mlkit:text-recognition-devanagari:16.0.0")

    // To recognize Japanese script
    implementation ("com.google.mlkit:text-recognition-japanese:16.0.0")

    // To recognize Korean script
    implementation ("com.google.mlkit:text-recognition-korean:16.0.0")

    //辨識後的畫面
    implementation ("androidx.compose.foundation:foundation:1.0.0")

    //傳送資料至google sheet
    implementation("com.android.volley:volley:1.2.1")

    //選單版面
    implementation ("androidx.navigation:navigation-ui-ktx")

}


