package com.example.marketsurveillance

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn


class GoogleDriveLauncher(private val activity: Activity) {

    private val REQUEST_PERMISSION_CODE = 123 //请求权限的请求码，唯一識別碼

    fun launchGoogleDriveWithPermissionCheck() {
        if (isUserSignedIn()) {
            // 用户已登录 Google 帐户，检查相册权限
            if (checkPermission()) {
                // 已授予相册权限，可以启动 Google Drive
                launchGoogleDrive()
            } else {
                // 未授予相册权限，请求权限
                showPermissionExplanationDialog()
                requestPermission()
            }
        } else {
            // 用户未登录 Google 帐户，你可以在这里处理
            showSignInRequiredDialog()
        }
    }

    private fun isUserSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        return account != null
    }

    //原本
//    private fun checkPermission(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            activity,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//    }
    //修改後
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        // 将权限请求结果传递给 GoogleDriveLauncher 类处理
//        googleDriveLauncher.onRequestPermissionsResult(requestCode, grantResults)
//    }
//    fun onRequestPermissionsResult(
//        requestCode: Int,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            REQUEST_PERMISSION_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // 用户授予了相册权限，启动 Google Drive
//                    launchGoogleDrive()
//                    val dialog = AlertDialog.Builder(activity)
//                        .setTitle("应用未安装")
//                        .setMessage("您需要安装 Google Drive 才能使用此功能。")
//                        .setPositiveButton("安装") { dialog, _ ->
//                            // 打开 Google Play 商店的 Google Drive 应用页面
//                            val playStoreIntent = Intent(Intent.ACTION_VIEW)
//                            playStoreIntent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.docs&hl=zh_TW&gl=US&pli=1")
//                            activity.startActivity(playStoreIntent)
//                            dialog.dismiss()
//                        }
//                        .setNegativeButton("取消") { dialog, _ ->
//                            // 用户取消安装，可以进行其他处理
//                            dialog.dismiss()
//                        }
//                        .create()
//                    dialog.show()
//                }
//            }
//        }
//    }

//    private fun launchGoogleDrive() {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com"))
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        activity.startActivity(intent)
//    } //他說這是察看檔案，所以跳回主畫面，但又是隱藏啟動的方法之一
//    private fun launchGoogleDrive() {
//        val intent = Intent(Intent.ACTION_OPEN)
//        intent.data = Uri.parse("https://drive.google.com")
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        activity.startActivity(intent)
//    }
    private fun launchGoogleDrive() {
        val packageName = "com.google.android.apps.docs" // Google Drive 应用的包名
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setPackage(packageName)
        if (isPackageInstalled(packageName)) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        } else {
            // 应用未安装，可以提示用户安装
            showGoogleDriveInstallDialog()
        }
    }

        private fun showGoogleDriveInstallDialog() {
            AlertDialog.Builder(activity)
                .setTitle("Google Drive 未安装")
                .setMessage("您需要安装 Google Drive 才能使用此功能。")
                .setPositiveButton("安装") { dialog, _ ->
                    // 打开 Google Play 商店的 Google Drive 应用页面
                    val playStoreIntent = Intent(Intent.ACTION_VIEW)
                    playStoreIntent.data =
                        Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.docs&hl=zh_TW&gl=US&pli=1")
                    activity.startActivity(playStoreIntent)
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    // 用户取消安装，可以进行其他处理
                    dialog.dismiss()
                }
                .show()
        }

        private fun isPackageInstalled(packageName: String): Boolean {
            val packageManager = activity.packageManager
            return packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) != null
        }

        private fun showPermissionExplanationDialog() {
            AlertDialog.Builder(activity)
                .setTitle("权限说明")
                .setMessage("为了上传您拍摄的照片到您的 Google Drive 帐户，我们需要访问您的相册。这样我们就可以从您的相册中选择并上传照片。我们将不会访问您的照片以外的任何内容。")
                .setPositiveButton("确定") { dialog, _ ->
                    // 点击确定按钮后，再次请求相册权限
                    requestPermission()
                    // 提示用户重新打开应用程序
                    Toast.makeText(activity, "请重新打开应用程序以使权限生效", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    // 点击取消按钮后，可以进行其他处理，比如关闭应用程序或提供其他功能
                    dialog.dismiss()
                }
                .show()
        }

        private fun showSignInRequiredDialog() {
            AlertDialog.Builder(activity)
                .setTitle("登录 Google 帐户")
                .setMessage("为了上传您的照片到 Google Drive，您需要先登录您的 Google 帐户。")
                .setPositiveButton("登录") { dialog, _ ->
                    // 点击登录按钮后，可以启动登录流程，例如使用 Google SignIn API
                    // 也可以跳转到登录界面
                    // 这里仅为示例，你需要根据你的应用程序需求进行实现
//                startSignInProcess()
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    // 点击取消按钮后，可以进行其他处理，比如关闭应用程序或提供其他功能
                    dialog.dismiss()
                }
                .show()
        }
//    private fun startSignInProcess() {
//        // 创建 Google SignIn Intent
//        val signInIntent = googleSignInClient.signInIntent
//        // 启动 Google SignIn Intent
//        activity.startActivity(signInIntent)
//    }
}
