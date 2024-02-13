package com.example.marketsurveillance

//import com.google.android.gms.drive.Drive
//import com.google.api.services.drive.Drive
//import com.google.api.client.googleapis.json.GoogleJsonResponseException
//import com.google.api.client.http.HttpRequestInitializer
//import com.google.api.client.http.javanet.NetHttpTransport
////import com.google.auth.http.HttpCredentialsAdapter
////import com.google.auth.oauth2.GoogleCredentials
//import java.io.IOException
//import java.io.OutputStream

//--
//import android.provider.MediaStore
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import java.io.ByteArrayOutputStream
import java.util.Collections
import java.util.UUID

class launchGoogleDriveWithPermissionCheck : AppCompatActivity() {

    private lateinit var client: GoogleSignInClient
    private lateinit var googleDriveServiceFunction: GoogleDriveServiceFunction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                requestSignIn()
            }
        }
    }

    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }

    private fun requestSignIn() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope(DriveScopes.DRIVE_FILE),
                Scope(DriveScopes.DRIVE_APPDATA),
                Scope(DriveScopes.DRIVE)
            )
            .build()

        client = GoogleSignIn.getClient(this@launchGoogleDriveWithPermissionCheck, signInOptions)
        val signInIntent = client.signInIntent

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                data?.let { handleSignInIntent(it, this@launchGoogleDriveWithPermissionCheck) } // Pass context
            }
        }
        launcher.launch(signInIntent)
    }
//原本
//    private fun handleSignInIntent(data: Intent) {
//        GoogleSignIn.getSignedInAccountFromIntent(data)
//            .addOnSuccessListener { googleSignInAccount ->
//                val credential = GoogleAccountCredential
//                    .usingOAuth2(this@launchGoogleDriveWithPermissionCheck, Collections.singleton(DriveScopes.DRIVE_FILE))
//
//                credential.selectedAccount = googleSignInAccount.account
//
//                drive(credential)
//            }
//            .addOnFailureListener { e -> e.printStackTrace() }
//    }
    private fun handleSignInIntent(data: Intent, context: Context) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnSuccessListener { googleSignInAccount ->
                val credential = GoogleAccountCredential
                    .usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE))

                credential.selectedAccount = googleSignInAccount.account

                drive(context, credential)
            }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

//    private fun drive(credential: GoogleAccountCredential) {
//        val googleDriveService = Drive.Builder(
//            AndroidHttp.newCompatibleTransport(),
//            GsonFactory(),
//            credential
//        )
//            .setApplicationName("Appname")
//            .build()
//
//        googleDriveServiceFunction = GoogleDriveServiceFunction(googleDriveService)
//    }
    private fun drive(context: Context, credential: GoogleAccountCredential) {
        val googleDriveService = Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName("Appname")
            .build()

        googleDriveServiceFunction = GoogleDriveServiceFunction(context, googleDriveService)
    }
}



class GoogleDriveServiceFunction(private val context: Context, private val driveService: Drive) {
    fun uploadImageToDrive(uri: Uri) {
        val outputStream = ByteArrayOutputStream()
        context.contentResolver.openInputStream(uri)?.use { input ->
            input.copyTo(outputStream)
        }

        val fileMetadata = File().apply {
            name = "image_${UUID.randomUUID()}.jpg"
        }
        val mediaContent = ByteArrayContent("image/jpeg", outputStream.toByteArray())

        val file = driveService.files().create(fileMetadata, mediaContent)
            .setFields("id")
            .execute()

        Log.d(TAG, "File ID: ${file.id}")
    }
}


//--
//private fun requestSignIn() {
//    //GoogleSignInOptions 被用來配置 Google 登入選項。
//    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestEmail()
//        //Google Drive 檔案放置的區域分為 Drive、AppDataFolder、Photos 三部份
//        .requestScopes(Scope(DriveScopes.DRIVE_FILE),
//            Scope(DriveScopes.DRIVE_APPDATA))
//        .requestIdToken("906220912489-kkiglmrp505se9n89telqjpngiir3cp6.apps.googleusercontent.com")
//        .build()
//    val client = GoogleSignIn.getClient(requireActivity(), signInOptions)
//
//    // The result of the sign-in Intent is handled in onActivityResult.
//    startActivityForResult(client.signInIntent, REQUEST_CODE_SIGN_IN)
//}
//
//
////它檢查返回的 requestCode 是否為 REQUEST_CODE_SIGN_IN，如果是，則調用 handleSignInResult 方法處理結果。
////handleSignInResult 方法從 Intent 中獲取已簽入的 Google 帳戶，並在成功簽入時使用帳戶來初始化 Google Drive 服務。如果簽入失敗，則記錄錯誤信息。
// override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    when (requestCode) {
//        REQUEST_CODE_SIGN_IN -> handleSignInResult(data)
//        else -> super.onActivityResult(requestCode, resultCode, data)
//    }
//}
//
//private fun handleSignInResult(result: Intent?) {
//    GoogleSignIn.getSignedInAccountFromIntent(result)
//        .addOnSuccessListener { googleAccount ->
//            Log.d(TAG, "Signed in as ${googleAccount.email}")
//
//            // Use the authenticated account to sign in to the Drive service.
//            val credential = GoogleAccountCredential.usingOAuth2(
//                requireActivity(),
//                setOf(DriveScopes.DRIVE_FILE)
//            )
//            credential.selectedAccount = googleAccount.account
//            val googleDriveService = Drive.Builder(
//                AndroidHttp.newCompatibleTransport(),
//                GsonFactory(),
//                credential
//            )
//                .setApplicationName("AppName")
//                .build()
//
//            // The DriveServiceHelper encapsulates all REST API and SAF functionality.
//            // Its instantiation is required before handling any onClick actions.
//            mDriveServiceHelper = DriveServiceHelper(googleDriveService)
//        }
//        .addOnFailureListener { exception ->
//            Log.e(TAG, "Unable to sign in.", exception)
//        }
//}
////上傳檔案
//val fileMetadata1 = File().apply {
//    name = "photo.jpg"
//}
//val filePath1 = java.io.File("files/photo.jpg")
//val mediaContent1 = FileContent("image/jpeg", filePath1)
//val file1 = driveService.files().create(fileMetadata1, mediaContent1)
//    .setFields("id")
//    .execute()
//println("File ID: ${file1.id}")

//val fileMetadata2 = File().apply {
//    name = "config.json"
//    parents = Collections.singletonList("appDataFolder")
//}
//val filePath2 = java.io.File("files/config.json")
//val mediaContent2 = FileContent("application/json", filePath2)
//val file2 = driveService.files().create(fileMetadata2, mediaContent2)
//    .setFields("id")
//    .execute()
//println("File ID: ${file2.id}")
//--
//會跳回主葉面
//class GoogleDriveLauncher(private val activity: Activity) {
//
//    private val REQUEST_PERMISSION_CODE = 123 //请求权限的请求码，唯一識別碼
//
//    fun launchGoogleDriveWithPermissionCheck() {
//        if (isUserSignedIn()) {
//            // 用户已登录 Google 帐户，检查相册权限
//            if (checkPermission()) {
//                // 已授予相册权限，可以启动 Google Drive
//                launchGoogleDrive()
//            } else {
//                // 未授予相册权限，请求权限
//                showPermissionExplanationDialog()
//                requestPermission()
//            }
//        } else {
//            // 用户未登录 Google 帐户，你可以在这里处理
//            showSignInRequiredDialog()
//        }
//    }
//
//    private fun isUserSignedIn(): Boolean {
//        val account = GoogleSignIn.getLastSignedInAccount(activity)
//        return account != null
//    }
//
//    //原本
////    private fun checkPermission(): Boolean {
////        return ContextCompat.checkSelfPermission(
////            activity,
////            Manifest.permission.READ_EXTERNAL_STORAGE
////        ) == PackageManager.PERMISSION_GRANTED
////    }
//    //修改後
//    private fun checkPermission(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            activity,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(
//            activity,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            activity,
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//            REQUEST_PERMISSION_CODE
//        )
//    }
//
////    override fun onRequestPermissionsResult(
////        requestCode: Int,
////        permissions: Array<String>,
////        grantResults: IntArray
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        // 将权限请求结果传递给 GoogleDriveLauncher 类处理
////        googleDriveLauncher.onRequestPermissionsResult(requestCode, grantResults)
////    }
////    fun onRequestPermissionsResult(
////        requestCode: Int,
////        grantResults: IntArray
////    ) {
////        when (requestCode) {
////            REQUEST_PERMISSION_CODE -> {
////                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    // 用户授予了相册权限，启动 Google Drive
////                    launchGoogleDrive()
////                    val dialog = AlertDialog.Builder(activity)
////                        .setTitle("应用未安装")
////                        .setMessage("您需要安装 Google Drive 才能使用此功能。")
////                        .setPositiveButton("安装") { dialog, _ ->
////                            // 打开 Google Play 商店的 Google Drive 应用页面
////                            val playStoreIntent = Intent(Intent.ACTION_VIEW)
////                            playStoreIntent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.docs&hl=zh_TW&gl=US&pli=1")
////                            activity.startActivity(playStoreIntent)
////                            dialog.dismiss()
////                        }
////                        .setNegativeButton("取消") { dialog, _ ->
////                            // 用户取消安装，可以进行其他处理
////                            dialog.dismiss()
////                        }
////                        .create()
////                    dialog.show()
////                }
////            }
////        }
////    }
//
////    private fun launchGoogleDrive() {
////        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com"))
////        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
////        activity.startActivity(intent)
////    } //他說這是察看檔案，所以跳回主畫面，但又是隱藏啟動的方法之一
////    private fun launchGoogleDrive() {
////        val intent = Intent(Intent.ACTION_OPEN)
////        intent.data = Uri.parse("https://drive.google.com")
////        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
////        activity.startActivity(intent)
////    }
//    private fun launchGoogleDrive() {
//        val packageName = "com.google.android.apps.docs" // Google Drive 应用的包名
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.setPackage(packageName)
//        if (isPackageInstalled(packageName)) {
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            activity.startActivity(intent)
//        } else {
//            // 应用未安装，可以提示用户安装
//            showGoogleDriveInstallDialog()
//        }
//    }
//
//        private fun showGoogleDriveInstallDialog() {
//            AlertDialog.Builder(activity)
//                .setTitle("Google Drive 未安装")
//                .setMessage("您需要安装 Google Drive 才能使用此功能。")
//                .setPositiveButton("安装") { dialog, _ ->
//                    // 打开 Google Play 商店的 Google Drive 应用页面
//                    val playStoreIntent = Intent(Intent.ACTION_VIEW)
//                    playStoreIntent.data =
//                        Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.docs&hl=zh_TW&gl=US&pli=1")
//                    activity.startActivity(playStoreIntent)
//                    dialog.dismiss()
//                }
//                .setNegativeButton("取消") { dialog, _ ->
//                    // 用户取消安装，可以进行其他处理
//                    dialog.dismiss()
//                }
//                .show()
//        }
//
//        private fun isPackageInstalled(packageName: String): Boolean {
//            val packageManager = activity.packageManager
//            return packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) != null
//        }
//
//        private fun showPermissionExplanationDialog() {
//            AlertDialog.Builder(activity)
//                .setTitle("权限说明")
//                .setMessage("为了上传您拍摄的照片到您的 Google Drive 帐户，我们需要访问您的相册。这样我们就可以从您的相册中选择并上传照片。我们将不会访问您的照片以外的任何内容。")
//                .setPositiveButton("确定") { dialog, _ ->
//                    // 点击确定按钮后，再次请求相册权限
//                    requestPermission()
//                    // 提示用户重新打开应用程序
//                    Toast.makeText(activity, "请重新打开应用程序以使权限生效", Toast.LENGTH_SHORT)
//                        .show()
//                    dialog.dismiss()
//                }
//                .setNegativeButton("取消") { dialog, _ ->
//                    // 点击取消按钮后，可以进行其他处理，比如关闭应用程序或提供其他功能
//                    dialog.dismiss()
//                }
//                .show()
//        }
//
//        private fun showSignInRequiredDialog() {
//            AlertDialog.Builder(activity)
//                .setTitle("登录 Google 帐户")
//                .setMessage("为了上传您的照片到 Google Drive，您需要先登录您的 Google 帐户。")
//                .setPositiveButton("登录") { dialog, _ ->
//                    // 点击登录按钮后，可以启动登录流程，例如使用 Google SignIn API
//                    // 也可以跳转到登录界面
//                    // 这里仅为示例，你需要根据你的应用程序需求进行实现
////                startSignInProcess()
//                    dialog.dismiss()
//                }
//                .setNegativeButton("取消") { dialog, _ ->
//                    // 点击取消按钮后，可以进行其他处理，比如关闭应用程序或提供其他功能
//                    dialog.dismiss()
//                }
//                .show()
//        }
////    private fun startSignInProcess() {
////        // 创建 Google SignIn Intent
////        val signInIntent = googleSignInClient.signInIntent
////        // 启动 Google SignIn Intent
////        activity.startActivity(signInIntent)
////    }
//}
