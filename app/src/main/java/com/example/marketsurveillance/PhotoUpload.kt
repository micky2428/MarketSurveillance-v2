package com.example.marketsurveillance

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户授予了相册权限，启动 Google Drive
                    launchGoogleDrive()
                } else {
                    // 用户拒绝了相册权限请求，你可以在这里处理
                    showPermissionExplanationDialog()
                }
            }
        }
    }

    private fun launchGoogleDrive() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }
    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(activity)
            .setTitle("权限说明")
            .setMessage("为了上传您拍摄的照片到您的 Google Drive 帐户，我们需要访问您的相册。这样我们就可以从您的相册中选择并上传照片。我们将不会访问您的照片以外的任何内容。")
            .setPositiveButton("确定") { dialog, _ ->
                // 点击确定按钮后，再次请求相册权限
                requestPermission()
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
