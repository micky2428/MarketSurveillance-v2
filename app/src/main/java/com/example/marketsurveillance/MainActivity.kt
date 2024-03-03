//@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketsurveillance

//import android.os.Build
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.result.IntentSenderRequest
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.example.marketsurveillance.login.MainEffect
//import com.example.marketsurveillance.login.MainEvent
//import com.example.marketsurveillance.login.MainViewModel
//import com.example.marketsurveillance.ui.theme.IntegrateGoogleDriveTheme

//---文字辨識模型
//*
//* Copyright 2020 Google LLC. All rights reserved.
//*
//* Licensed under the Apache License, Version 2.0 (the "License");
//* you may not use this file except in compliance with the License.
//* You may obtain a copy of the License at
//*
//*     http://www.apache.org/licenses/LICENSE-2.0
//*
//* Unless required by applicable law or agreed to in writing, software
//* distributed under the License is distributed on an "AS IS" BASIS,
//* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//* See the License for the specific language governing permissions and
//* limitations under the License.
//*/

//package com.google.mlkit.vision.demo
//
//import android.Manifest
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.marketsurveillance.textdetector.ChooserActivity
//
//class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_vision_entry_choice) //外觀的檔案
//
////        findViewById<TextView>(R.id.java_entry_point).setOnClickListener {
////            val intent = Intent(this@EntryChoiceActivity, ChooserActivity::class.java)
////            startActivity(intent)
////        }
//
//        findViewById<TextView>(R.id.kotlin_entry_point).setOnClickListener {
//            val intent =
//                Intent(
//                    this@MainActivity,
//                    com.example.marketsurveillance.textdetector.ChooserActivity::class.java
//                )
//            startActivity(intent)
//        }
//
//        if (!allRuntimePermissionsGranted()) {
//            getRuntimePermissions()
//        }
//    }
//
//    private fun allRuntimePermissionsGranted(): Boolean {
//        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
//            permission?.let {
//                if (!isPermissionGranted(this, it)) {
//                    return false
//                }
//            }
//        }
//        return true
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
//            ActivityCompat.requestPermissions(
//                this,
//                permissionsToRequest.toTypedArray(),
//                PERMISSION_REQUESTS
//            )
//        }
//    }
//
//    private fun isPermissionGranted(context: Context, permission: String): Boolean {
//        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.i(TAG, "Permission granted: $permission")
//            return true
//        }
//        Log.i(TAG, "Permission NOT granted: $permission")
//        return false
//    }
//
//    companion object {
//        private const val TAG = "EntryChoiceActivity"
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

//這段程式碼是用於檢查和獲取運行時權限的。在 Android 中，某些操作需要在運行時獲得用戶的許可，例如訪問相機、存儲設備、位置等。這段程式碼的作用是檢查應用程式所需的運行時權限是否已經被授予，如果未授予，則向用戶請求這些權限。
//allRuntimePermissionsGranted() 函數用於檢查是否所有所需的運行時權限都已經被授予。它遍歷 REQUIRED_RUNTIME_PERMISSIONS 列表中的每個權限，並檢查每個權限是否已經被授予。如果任何一個權限未被授予，則返回 false，否則返回 true。
//getRuntimePermissions() 函數用於獲取那些尚未被授予的運行時權限。它遍歷 REQUIRED_RUNTIME_PERMISSIONS 列表中的每個權限，並將未被授予的權限添加到 permissionsToRequest 列表中。然後，如果 permissionsToRequest 列表不為空，則通過 ActivityCompat.requestPermissions() 方法向用戶請求這些權限。

//


//
//20240215 有按鍵畫面

import android.content.Context
import android.content.Intent
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marketsurveillance.login.UploadActivity
import com.example.marketsurveillance.ui.theme.MarketSurveillanceTheme
import dagger.hilt.android.AndroidEntryPoint


//1130228缺便是
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
//    val activity = LocalContext.current as Activity
//    val activity = LocalContext.current as AppCompatActivity
//    val googleDriveLauncher = GoogleDriveLauncher(activity)
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
                        // 打開 Google Drive 應用程序
//                        googleDriveLauncher.launchGoogleDriveWithPermissionCheck()
//                        navController.navigate("GoogleDrive")
//                        startForResult.launch(getGoogleSignInClient(ctx).signInIntent)
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

//---

/* class to demonstrate use of Drive files list API */
//object DriveQuickstart {
//    /**
//     * Application name.
//     */
//    private const val APPLICATION_NAME = "MarketSurveillance"
//
//    /**
//     * Global instance of the JSON factory.
//     */
//    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
//
//    /**
//     * Directory to store authorization tokens for this application.
//     */
//    private const val TOKENS_DIRECTORY_PATH = "906220912489-kkiglmrp505se9n89telqjpngiir3cp6.apps.googleusercontent.com"
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private val SCOPES = listOf(DriveScopes.DRIVE_METADATA_READONLY)
//    private const val CREDENTIALS_FILE_PATH = "/credentials.json"
//
//    /**
//     * Creates an authorized Credential object.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */
//    @Throws(IOException::class)
//    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
//        // Load client secrets.
//        val `in` =
//            DriveQuickstart::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
//                ?: throw FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
//        val clientSecrets =
//            GoogleClientSecrets.load(
//                JSON_FACTORY,
//                InputStreamReader(`in`)
//            )
//
//        // Build flow and trigger user authorization request.
//        val flow =
//            GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
//            )
//                .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build()
//        val receiver: LocalServerReceiver = Builder().setPort(8888).build()
//        //returns an authorized Credential object.
//        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
//    }
//
//    @Throws(IOException::class, GeneralSecurityException::class)
//    @JvmStatic
//    fun main(args: Array<String>) {
//        // Build a new authorized API client service.
////        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
////        val service: Drive = Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
////            .setApplicationName(APPLICATION_NAME)
////            .build()
//        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
//        val service: Drive = Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//            .setApplicationName(APPLICATION_NAME)
//            .build()
//
//        // Print the names and IDs for up to 10 files.
//        val result: FileList = service.files().list()
//            .setPageSize(10)
//            .setFields("nextPageToken, files(id, name)")
//            .execute()
//        val files: List<DriveFile>? = result.files?.toList()?.map { it as DriveFile }
//        if (files == null || files.isEmpty()) {
//            println("No files found.")
//
//        } else {
//            println("Files:")
//            for (file in files) {
//                System.out.printf("%s (%s)\n", file.getName(), file.getId())


//    }
//}

//@Composable
//fun MarketCheckScreen() {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colors.background
//    ) {
//        Text(text = "請輸入商品資訊", fontSize = 30.sp)
//    }
//}

//@Composable
//fun MarketSurveillanceTheme(content: @Composable () -> Unit) {
//    MaterialTheme(
//        colors = MaterialTheme.colors,
//        typography = MaterialTheme.typography,
//        shapes = MaterialTheme.shapes,
//        content = content
//    )
//}
//
//fun getGoogleSignInClient(context: Context): GoogleSignInClient {
//    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestEmail()
//        .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE))
//        .build()
//
//    return GoogleSignIn.getClient(context, signInOptions)
//}
//
//val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//    if (result.resultCode == Activity.RESULT_OK) {
//        val intent = result.data
//        if (result.data != null) {
//            val task: Task<GoogleSignInAccount> =
//                GoogleSignIn.getSignedInAccountFromIntent(intent)
//
//            /**
//             * handle [task] result
//             */
//        } else {
//            Toast.makeText(ctx, "Google Login Error!", Toast.LENGTH_LONG).show()
//        }
//    }
//}
//
//GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
//
//    // get credentials
//    val credential = GoogleAccountCredential.usingOAuth2(
//        context, listOf(DriveScopes.DRIVE, DriveScopes.DRIVE_FILE)
//    )
//    credential.selectedAccount = googleAccount.account!!
//
//    // get Drive Instance
//    val drive = Drive
//        .Builder(
//            AndroidHttp.newCompatibleTransport(),
//            JacksonFactory.getDefaultInstance(),
//            credential
//        )
//        .setApplicationName(context.getString(R.string.app_name))
//        .build()
//}
//
//viewModelScope.launch(Dispatchers.IO) {
//    // Define a Folder
//    val gFolder = com.google.api.services.drive.model.File()
//    // Set file name and MIME
//    gFolder.name = "My Cool Folder Name"
//    gFolder.mimeType = "application/vnd.google-apps.folder"
//
//    // You can also specify where to create the new Google folder
//    // passing a parent Folder Id
//    val parents: MutableList<String> = ArrayList(1)
//    parents.add("your_parent_folder_id_here")
//    gFolder.parents = parents
//    drive.Files().create(gFolder).setFields("id").execute()
//}
//
//for (file in files) {
//    val gfile = com.google.api.services.drive.model.File()
//
//    val fileContent = FileContent("your_mime", file)
//    gfile.name = file.name
//
//    val parents: MutableList<String> = ArrayList(1)
//    parents.add("folder_id") // Here you need to get the parent folder id
//
//    gfile.parents = parents
//
//    drive.Files().create(gfile, fileContent).setFields("id").execute()
//}
////把photoupload內容移過來還是錯誤
//@Composable
//fun MyApp(content: @Composable () -> Unit) {
//    MaterialTheme {
//        Surface(modifier = Modifier.fillMaxSize()) {
//            content()
//        }
//    }
//}
//
//
//@Composable
//fun launchGoogleDriveWithPermissionCheck() {
//    val context = LocalContext.current
//    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestEmail()
//        .requestScopes(
//            Scope(DriveScopes.DRIVE_FILE),
//            Scope(DriveScopes.DRIVE_APPDATA),
//            Scope(DriveScopes.DRIVE)
//        )
//        .build()
//
//    val client = GoogleSignIn.getClient(context, signInOptions)
//    val signInIntent = client.signInIntent
//
//    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == ComponentActivity.RESULT_OK) {
//            val data = result.data
//            data?.let {
//                handleSignInIntent(it, context)
//            }
//        }
//    }
//    launcher.launch(signInIntent)
//}
//
//private fun handleSignInIntent(data: Intent, context: Context) {
//    GoogleSignIn.getSignedInAccountFromIntent(data)
//        .addOnSuccessListener { googleSignInAccount ->
//            val credential = GoogleAccountCredential
//                .usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE))
//
//            credential.selectedAccount = googleSignInAccount.account
//
//            drive(context, credential)
//            val driveWebIntent =
//                Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com"))
//            context.startActivity(driveWebIntent)
//        }
//        .addOnFailureListener { e ->
//            e.printStackTrace()
//            // 在這裡添加顯示錯誤消息的代碼，例如：
//            Toast.makeText(context, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//}
//
//private fun drive(context: Context, credential: GoogleAccountCredential) {
//    try {
//        val googleDriveService = Drive.Builder(
//            AndroidHttp.newCompatibleTransport(),
//            GsonFactory(),
//            credential
//        )
//            .setApplicationName("MarketSurveillance")
//            .build()
//
//        // 在這裡執行相應的操作，例如上傳、下載文件等
//        // googleDriveServiceFunction = GoogleDriveServiceFunction(context, googleDriveService)
//    } catch (e: Exception) {
//        // 在這裡處理異常
//        e.printStackTrace()
//        // 例如：顯示一條錯誤消息給用戶
//        Toast.makeText(
//            context,
//            "Error creating Drive service: ${e.message}",
//            Toast.LENGTH_SHORT
//        )
//            .show()
//    }
//}
