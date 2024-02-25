//@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.marketsurveillance

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marketsurveillance.login.MainEffect
import com.example.marketsurveillance.login.MainEvent
import com.example.marketsurveillance.login.MainViewModel
import com.example.marketsurveillance.ui.theme.IntegrateGoogleDriveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntegrateGoogleDriveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<MainViewModel>()
                    val state by viewModel.state.collectAsState()
                    val effect by viewModel.effect.collectAsState()

                    var showDialog by remember {
                        mutableStateOf(false)
                    }
                    val context = LocalContext.current
                    val signInLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = {
                            viewModel.onEvent(
                                MainEvent.OnSignInResult(
                                    it.data ?: return@rememberLauncherForActivityResult
                                )
                            )
                        }
                    )
                    val authorizeLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = {
                            viewModel.onEvent(
                                MainEvent.OnAuthorize(
                                    it.data ?: return@rememberLauncherForActivityResult
                                )
                            )
                        }
                    )

                    val pickerPhotoLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = {
                            viewModel.onEvent(
                                MainEvent.Backup(
                                    it ?: return@rememberLauncherForActivityResult
                                )
                            )
                        }
                    )
                    LaunchedEffect(key1 = effect) {
                        when (effect) {
                            is MainEffect.Authorize -> {
                                authorizeLauncher.launch(
                                    IntentSenderRequest.Builder((effect as MainEffect.Authorize).intentSender)
                                        .build()
                                )
                            }

                            is MainEffect.SignIn -> {
                                signInLauncher.launch(
                                    IntentSenderRequest.Builder((effect as MainEffect.SignIn).intentSender)
                                        .build()
                                )
                            }

                            null -> Unit

                        }
                    }
                    if (showDialog) {
                        LaunchedEffect(key1 = true) {
                            viewModel.onEvent(MainEvent.GetFiles)
                        }
                        Dialog(onDismissRequest = { showDialog = false }) {
                            Surface(tonalElevation = 4.dp) {
                                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                                    items(state.restoreFiles) {
                                        Box(modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                viewModel.onEvent(MainEvent.Restore(it.id))
                                                Toast
                                                    .makeText(context, "Saved to local storage", Toast.LENGTH_SHORT)
                                                    .show()
                                                showDialog = false
                                            }) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext
                                                    .current).data(it.thumbnailFileLink).placeholder(R.drawable.drive_icon).build(),
                                                contentDescription = "",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.size(150.dp)
                                            )
                                            Text(
                                                text = it.nameFile, modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        Brush.linearGradient(
                                                            listOf(Color.Black, Color.Transparent)
                                                        )
                                                    )
                                                    .align(Alignment.BottomCenter)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.drive_icon),
                            modifier = Modifier.size(80.dp),
                            contentDescription = ""
                        )
                        Text(text = "Backup Drive")
                        if (state.email == null) {
                            Button(onClick = { viewModel.onEvent(MainEvent.SignInGoogle) }) {
                                Text(text = "Sign in")
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(onClick = { pickerPhotoLauncher.launch(PickVisualMediaRequest()) }) {
                                    Text(text = "Backup")
                                }
                                Button(onClick = {showDialog= true  }) {
                                    Text(text = "Restore")
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Welcome ${state.email}")
                            Button(onClick = { viewModel.onEvent(MainEvent.SignOut) }) {
                                Text(text = "Sign out")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IntegrateGoogleDriveTheme {
        Greeting("Android")
    }
}


//
////https://github.com/philipplackner/CameraXGuide/blob/taking-photos/app/src/androidTest/java/com/plcoding/cameraxguide/ExampleInstrumentedTest.kt
//import android.Manifest
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.Matrix
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture.OnImageCapturedCallback
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.ImageProxy
//import androidx.camera.view.CameraController
//import androidx.camera.view.LifecycleCameraController
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Cameraswitch
//import androidx.compose.material.icons.filled.Photo
//import androidx.compose.material.icons.filled.PhotoCamera
//import androidx.compose.material3.BottomSheetScaffold
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.rememberBottomSheetScaffoldState
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.marketsurveillance.ui.theme.CameraXGuideTheme
//import kotlinx.coroutines.launch
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (!hasRequiredPermissions()) {
//            ActivityCompat.requestPermissions(
//                this, CAMERAX_PERMISSIONS, 0 //requestCode可以自己設定數字
//            )
//        }
//        setContent {
//            CameraXGuideTheme {
//                val scope = rememberCoroutineScope()
//                val scaffoldState = rememberBottomSheetScaffoldState()
//                val controller = remember { //camerapreview.kt
//                    LifecycleCameraController(applicationContext).apply {
//                        setEnabledUseCases( //決定要有那些功能
//                            CameraController.IMAGE_CAPTURE or
//                                    CameraController.VIDEO_CAPTURE
//                        )
//                    }
//                }
//                val viewModel = viewModel<MainViewModel>()
//                val bitmaps by viewModel.bitmaps.collectAsState()
//                //Bottom Sheet 是一種從屏幕底部彈出的可互動的半透明面板，通常用於顯示補充信息、操作或菜單
//                BottomSheetScaffold(
//                    scaffoldState = scaffoldState,
//                    sheetPeekHeight = 0.dp,
//                    sheetContent = {
//                        PhotoBottomSheetContent(  //photobottomsheet.kt
//                            bitmaps = bitmaps,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        )
//                    }
//                ) { padding ->
//                    Box(  //建立畫面邊框
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(padding)
//                    ) {
//                        CameraPreview( //camerapreview.kt
//                            controller = controller,
//                            modifier = Modifier
//                                .fillMaxSize()
//                        )
//
//                        IconButton(
//                            onClick = {
//                                controller.cameraSelector = //左上角前後鏡頭轉換
//                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
//                                        CameraSelector.DEFAULT_FRONT_CAMERA
//                                    } else CameraSelector.DEFAULT_BACK_CAMERA
//                            },
//                            modifier = Modifier
//                                .offset(16.dp, 16.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Cameraswitch,
//                                contentDescription = "Switch camera"
//                            )
//                        }
//
//                        Row( //下方選單
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .align(Alignment.BottomCenter)
//                                .padding(16.dp),
//                            horizontalArrangement = Arrangement.SpaceAround
//                        ) {
//                            IconButton( //打開app內相簿
//                                onClick = {
//                                    scope.launch {
//                                        scaffoldState.bottomSheetState.expand()
//                                    }
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Photo,
//                                    contentDescription = "Open gallery"
//                                )
//                            }
//                            IconButton( //照相
//                                onClick = {
//                                    takePhoto(
//                                        controller = controller,
//                                        onPhotoTaken = viewModel::onTakePhoto
//                                    )
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.PhotoCamera,
//                                    contentDescription = "Take photo"
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun takePhoto(
//        controller: LifecycleCameraController,
//        onPhotoTaken: (Bitmap) -> Unit
//    ) {
//        controller.takePicture(
//            ContextCompat.getMainExecutor(applicationContext),
//            object : OnImageCapturedCallback() {
//                override fun onCaptureSuccess(image: ImageProxy) {
//                    super.onCaptureSuccess(image)
//
//                    val matrix = Matrix().apply {
//                        postRotate(image.imageInfo.rotationDegrees.toFloat())
//                    }
//                    val rotatedBitmap = Bitmap.createBitmap(
//                        image.toBitmap(),
//                        0,
//                        0,
//                        image.width,
//                        image.height,
//                        matrix,
//                        true
//                    )
//
//                    onPhotoTaken(rotatedBitmap)
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    super.onError(exception)
//                    Log.e("Camera", "Couldn't take photo: ", exception)
//                }
//            }
//        )
//    }
//
//    private fun hasRequiredPermissions(): Boolean {
//        return CAMERAX_PERMISSIONS.all {
//            ContextCompat.checkSelfPermission(
//                applicationContext,
//                it
//            ) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    companion object {
//        private val CAMERAX_PERMISSIONS = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//        )
//    }
//}



//
////20240215 有按鍵畫面
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.Button
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.marketsurveillance.ui.theme.MarketSurveillanceTheme
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
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
//    }
//}
//
//@Composable
//fun MainScreen(navController: NavHostController) {
////    val activity = LocalContext.current as Activity
////    val activity = LocalContext.current as AppCompatActivity
////    val googleDriveLauncher = GoogleDriveLauncher(activity)
////    val context = LocalContext.current
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colors.background
//    ) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Column(
//                modifier = Modifier.align(Alignment.Center),
//                verticalArrangement = Arrangement.Center
//            ) {
//                // 添加“市場檢查”按鈕
//                GreetingButton(
//                    name = "市場檢查",
//                    onClick = {
//                        // 导航到市场检查页面
//                        navController.navigate("ProductInfo")
//                    },
//                    backgroundColor = Color(0xFFFF7F50),
//                    contentColor = Color.White,
//                    modifier = Modifier.padding(vertical = 10.dp),
//                    fontSize = 50.sp
//                )
//
//                // 添加“拍照上傳雲端”按鈕
//                GreetingButton(
//                    name = "拍照上傳雲端",
//                    onClick = {
//
//                        // 打開 Google Drive 應用程序
////                        googleDriveLauncher.launchGoogleDriveWithPermissionCheck()
////                        navController.navigate("GoogleDrive")
////                        startForResult.launch(getGoogleSignInClient(ctx).signInIntent)
//                    },
//                    backgroundColor = Color(0xFFFFA500),
//                    contentColor = Color.White,
//                    modifier = Modifier.padding(vertical = 10.dp),
//                    fontSize = 50.sp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun GreetingButton(
//    name: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    backgroundColor: Color = Color.Blue,
//    contentColor: Color = Color.White,
//    fontSize: TextUnit = 55.sp
//) {
//    Button(
//        onClick = onClick,
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = backgroundColor,
//            contentColor = contentColor
//        ),
//        modifier = modifier
//    ) {
//        Text(
//            text = name,
//            style = TextStyle(
//                fontSize = fontSize,
//                fontWeight = FontWeight.Bold
//            )
//        )
//    }
//}

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
