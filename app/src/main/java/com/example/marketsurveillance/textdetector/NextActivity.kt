package com.example.marketsurveillance.textdetector

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//class NextActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val resultText = intent.getStringExtra("resultText")
//        setContent {
//            Surface {
//                ShowResultText(resultText ?: "")
//            }
//        }
//    }
//}
//
//@Composable
//fun ShowResultText(resultText: String) {
//    val context = LocalContext.current
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = resultText,
//            fontSize = 16.sp,
//            textAlign = TextAlign.Start,
//            modifier = Modifier.clickable {
//                // 复制文本到剪贴板
//                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                val clip = ClipData.newPlainText("Copied Text", resultText)
//                clipboard.setPrimaryClip(clip)
//                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
//            }
//        )
//    }
//}





class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultText = intent.getStringExtra("resultText")
        setContent {
            Surface {
                ShowResultText(resultText ?: "") {
                    // 当用户点击返回按钮时关闭当前页面
                    finish()
                }
            }
        }
    }
}

@Composable
fun ShowResultText(resultText: String, onBackClick: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = resultText,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                // 复制文本到剪贴板
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", resultText)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        )
        // 返回按钮
        Button(
            onClick = onBackClick, // 点击事件，调用传入的 onBackClick 函数
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back")
        }
    }
}