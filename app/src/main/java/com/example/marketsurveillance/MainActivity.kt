//@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketsurveillance





//
//20240215 有按鍵畫面
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marketsurveillance.login.UploadActivity
import com.example.marketsurveillance.ui.theme.MarketSurveillanceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isContentSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0 //requestCode可以自己設定數字
            )
        }
        setContent {
            // 设置内容
            setContent()
        }
    }



    private fun setContent() {
        setContent {
            MarketSurveillanceTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(navController)
                    }
                    composable("ProductInfo") {
                        MarketCheckScreen()
                    }
                }
            }
        }
        isContentSet = true
    }
    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all { permission: String ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 寫入外部存儲權限
            Manifest.permission.RECORD_AUDIO
        )
    }
}
//1130228有權限請求-錯誤示範
//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//    private var isContentSet = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            // 检查和设置内容
//            checkPermissionsAndSetContent()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // 检查权限
//        checkPermissions()
//    }
//
//    private fun checkPermissionsAndSetContent() {
//        if (!allRuntimePermissionsGranted()) {
//            // 如果未授予所有运行时权限，则请求权限
//            getRuntimePermissions()
//        } else {
//            // 如果已授予所有运行时权限，并且内容尚未设置，则设置内容
//            if (!isContentSet) {
//                setContent()
//            }
//        }
//    }
//
//    private fun setContent() {
//        setContent {
//            MarketSurveillanceTheme {
//                val navController = rememberNavController()
//                NavHost(navController, startDestination = "main") {
//                    composable("main") {
//                        MainScreen(navController)
//                    }
//                    composable("ProductInfo") {
//                        MarketCheckScreen()
//                    }
//                }
//            }
//        }
//        isContentSet = true
//    }
//
//    private fun checkPermissions() {
//        if (!allRuntimePermissionsGranted()) {
//            // 如果未授予所有运行时权限，则请求权限
//            getRuntimePermissions()
//        }
//    }
//
//    private fun allRuntimePermissionsGranted(): Boolean {
//        return REQUIRED_RUNTIME_PERMISSIONS.all { permission ->
//            permission?.let {
//                isPermissionGranted(this, it)
//            } ?: true
//        }
//    }
//
//    private fun getRuntimePermissions() {
//        val permissionsToRequest = ArrayList<String>()
//        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
//            permission?.let {
//                if (!isPermissionGranted(this, it)) {
//                    permissionsToRequest.add(permission)
//                }
//            }
//        }
//
//        if (permissionsToRequest.isNotEmpty()) {
//            // 请求权限
//            ActivityCompat.requestPermissions(
//                this,
//                permissionsToRequest.toTypedArray(),
//                PERMISSION_REQUESTS
//            )
//        } else {
//            // 如果权限已全部授予，并且内容尚未设置，则设置内容
//            if (!isContentSet) {
//                setContent()
//            }
//        }
//    }
//
//    private fun isPermissionGranted(context: Context, permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    companion object {
//        private const val PERMISSION_REQUESTS = 1
//
//        private val REQUIRED_RUNTIME_PERMISSIONS =
//            arrayOf(
//                Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//    }
//}



@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current //for UploadActivity
    fun startUploadActivity(context: Context) {
        val intent = Intent(context, UploadActivity::class.java)
        context.startActivity(intent)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                // 添加“市場檢查”按鈕
                GreetingButton(
                    name = "市場檢查",
                    onClick = {
                        // 导航到市场检查页面
                        navController.navigate("ProductInfo")
                    },
                    backgroundColor = Color(0xFFFF7F50),
                    contentColor = Color.White,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 50.sp
                )

                // 添加“照片上傳雲端”按鈕
                GreetingButton(

                    name = "照片上傳雲端",
                    onClick = {
                        startUploadActivity(context)
                    },
                    backgroundColor = Color(0xFFFFA500),
                    contentColor = Color.White,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 50.sp
                )
            }
        }
    }
}

@Composable
fun GreetingButton(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Blue,
    contentColor: Color = Color.White,
    fontSize: TextUnit = 55.sp
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = modifier
    ) {
        Text(
            text = name,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        )
        }
    }


