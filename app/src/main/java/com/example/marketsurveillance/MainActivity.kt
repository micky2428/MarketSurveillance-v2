package com.example.marketsurveillance


//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.ui.unit.dp
//import android.widget.Toast
//import androidx.compose.foundation.layout.Alignment
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketsurveillance.ui.theme.MarketSurveillanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MarketSurveillanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // 添加“市場檢查”按鈕
                        GreetingButton(
                            name = "市場檢查",
                            onClick = {
                                // 市場檢查按鈕點擊後的處理邏輯
                                // 這裡可以加入您想要的操作
                            },
                            backgroundColor = Color.Yellow,
                            contentColor = Color.White,
                            modifier = Modifier.padding(all = 10.dp)
                                .align(Alignment.CenterHorizontally),
//                                .align(Alignment.CenterVertically),
                            fontSize = 55.sp

                        )

                        // 添加“拍照上傳雲端”按鈕
                        GreetingButton(
                            name = "拍照上傳雲端",
                            onClick = {
                                // 拍照上傳雲端按鈕點擊後的處理邏輯
                                // 這裡可以加入您想要的操作
                            },
                            backgroundColor = Color.Yellow,
                            contentColor = Color.White,
                            modifier = Modifier.padding(all = 10.dp)
                                .align(Alignment.CenterHorizontally),
//                                .align(Alignment.CenterVertically),
                            fontSize = 55.sp
                        )
                    }
                }
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

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MarketSurveillanceTheme {
//        Greeting("Android")
//    }
//}
