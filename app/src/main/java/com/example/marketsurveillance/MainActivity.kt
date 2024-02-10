package com.example.marketsurveillance

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


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
                    backgroundColor = Color.Yellow,
                    contentColor = Color.White,
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 50.sp
                )

                // 添加“拍照上傳雲端”按鈕
                GreetingButton(
                    name = "拍照上傳雲端",
                    onClick = {
                        // 拍照上傳雲端按鈕點擊後的處理邏輯
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
