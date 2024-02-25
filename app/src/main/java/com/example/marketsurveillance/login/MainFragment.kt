package com.example.marketsurveillance.login


import com.example.marketsurveillance.backup.DriveFileInfo

data class MainState(
    val email:String? = null,
    val restoreFiles:List<DriveFileInfo> = emptyList()
)