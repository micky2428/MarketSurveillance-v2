plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}



android {
    namespace = "com.example.marketsurveillance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.marketsurveillance"
        minSdk = 26
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
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/{DEPENDENCIES}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-inappmessaging:20.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    //productInfo頁面
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("androidx.compose.foundation:foundation-layout:<version>")
    implementation ("androidx.compose.material:material:1.6.1")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.ui:ui:1.6.1")
    implementation ("androidx.compose.material3:material3:1.2.0")
    //photoupload頁面
    implementation ("androidx.appcompat:appcompat:1.6.1")
//    //google權限
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("org.eclipse.jetty:jetty-server:11.0.6")
    implementation ("com.google.oauth-client:google-oauth-client-java6:1.31.1")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation ("com.google.android.gms:play-services-drive:17.0.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")

    implementation ("com.google.api-client:google-api-client:1.31.0")
    implementation ("com.google.http-client:google-http-client-gson:1.42.3")
    implementation ("com.google.apis:google-api-services-docs:v1-rev20230929-2.0.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation ("com.google.guava:guava:24.1-jre")
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
    implementation("com.google.api-client:google-api-client-android:1.31.0")
    implementation("com.google.apis:google-api-services-drive:v3-rev136-1.25.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("com.android.volley:volley:1.2.1")


//最新認證方式
    implementation("androidx.credentials:credentials:1.3.0-alpha01")

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")
//1130216
    implementation ("com.fasterxml.jackson.core:jackson-core:2.12.5") // 根據您的需求選擇版本
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.12.5") // 根據您的需求選擇版本
    implementation ("com.fasterxml.jackson.core:jackson-annotations:2.12.5")
}


