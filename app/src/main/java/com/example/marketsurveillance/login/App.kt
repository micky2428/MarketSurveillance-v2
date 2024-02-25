package com.example.marketsurveillance.login

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App :Application(){
}

//在 Kotlin 中，@HiltAndroidApp 是一個註釋，用於標記應用程式類，告訴 Hilt 框架這是應用程式的入口點。通常，這個類會繼承自 Application 類，並在應用程式的生命週期中執行一些初始化設置。