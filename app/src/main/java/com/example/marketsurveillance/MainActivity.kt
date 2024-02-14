package com.example.marketsurveillance
//相簿
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.util.Collections

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
//    val context = LocalContext.current
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

                // 添加“拍照上傳雲端”按鈕
                GreetingButton(
                    name = "拍照上傳雲端",
                    onClick = {
                        // 打開 Google Drive 應用程序
//                        googleDriveLauncher.launchGoogleDriveWithPermissionCheck()
                            launchGoogleDriveWithPermissionCheck()
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

//@Composable
//fun MarketCheckScreen() {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colors.background
//    ) {
//        Text(text = "請輸入商品資訊", fontSize = 30.sp)
//    }
//}

@Composable
fun MarketSurveillanceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = MaterialTheme.colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}

class launchGoogleDriveWithPermissionCheck : AppCompatActivity() {

    private lateinit var client: GoogleSignInClient

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
        try {
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(
                    Scope(DriveScopes.DRIVE_FILE),
                    Scope(DriveScopes.DRIVE_APPDATA),
                    Scope(DriveScopes.DRIVE)
                )
                .build()

            client =
                GoogleSignIn.getClient(this@launchGoogleDriveWithPermissionCheck, signInOptions)
            val signInIntent = client.signInIntent

            val launcher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK) {
                        val data = result.data
                        data?.let {
                            handleSignInIntent(
                                it,
                                this@launchGoogleDriveWithPermissionCheck
                            )
                        } // Pass context
                    }
                }
            launcher.launch(signInIntent)
        } catch (e: Exception) {
            // 在這裡處理異常
            e.printStackTrace()
            Log.e("SignInError", "Error occurred during sign in: ${e.message}", e)
        }
    }


    private fun handleSignInIntent(data: Intent, context: Context) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnSuccessListener { googleSignInAccount ->
                val credential = GoogleAccountCredential
                    .usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE))

                credential.selectedAccount = googleSignInAccount.account

                drive(context, credential)
                val driveWebIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com"))
                context.startActivity(driveWebIntent)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                // 在這裡添加顯示錯誤消息的代碼，例如：
                Toast.makeText(context, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun drive(context: Context, credential: GoogleAccountCredential) {
        try {
            val googleDriveService = Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory(),
                credential
            )
                .setApplicationName("MarketSurveillance")
                .build()

            // 在這裡執行相應的操作，例如上傳、下載文件等
            // googleDriveServiceFunction = GoogleDriveServiceFunction(context, googleDriveService)
        } catch (e: Exception) {
            // 在這裡處理異常
            e.printStackTrace()
            // 例如：顯示一條錯誤消息給用戶
            Toast.makeText(
                context,
                "Error creating Drive service: ${e.message}",
                Toast.LENGTH_SHORT
            )
                .show()
        }


    }
}
