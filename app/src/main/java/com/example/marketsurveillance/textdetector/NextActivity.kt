package com.example.marketsurveillance.textdetector

//import androidx.compose.ui.platform.ClipboardManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern

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

        val productNameText = extractProductName(resultText ?: "")
        val productNumberText = extractproductNumber(resultText ?: "")
        val productBatchText = extractproductBatch(resultText ?: "")
        val producerNameText = extractproducerName(resultText ?: "")
        val bsmiNumberText = extractbsmiNumber(resultText ?: "")
        val productSourceCountryText = extractproductSourceCountry(resultText ?: "")

        setContent {
            val context = LocalContext.current
            Surface {
                ShowResultText(
                            productName = productNameText,
                            productNumber = productNumberText,
                            productBatch = productBatchText,
                            producerName = producerNameText,
                            bsmiNumber = bsmiNumberText,
                            productSourceCountry = productSourceCountryText,
                            resultText = resultText ?: "",
                            context = context
                        ) {
                            finish()
                        }
                    }
                }
            }
        }


@Composable
fun ShowResultText(
    productName: String,
    productNumber: String,
    productBatch: String,
    producerName: String,
    bsmiNumber: String,
    productSourceCountry: String,
    resultText: String,
    context: Context,
    onBackClick: () -> Unit
) {
    var productNameText by remember { mutableStateOf<String>("") }
    var productNumberText by remember { mutableStateOf<String>("") }
    var productBatchText by remember { mutableStateOf<String>("") }
    var producerNameText by remember { mutableStateOf<String>("") }
    var bsmiNumberText by remember { mutableStateOf<String>("") }
    var productSourceCountryText by remember { mutableStateOf<String>("") }


    LaunchedEffect(true) {
        productNameText = productName
        productNumberText = productNumber
        productBatchText = productBatch
        producerNameText = producerName
        bsmiNumberText = bsmiNumber
        productSourceCountryText = productSourceCountry

    }
// 包裹 Column 在 ScrollView 中
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextField(
            value = productNameText,
            onValueChange = { productNameText = it },
            label = { Text("品名") },
            modifier = Modifier.fillMaxWidth(), // 添加必要的修飾符
            textStyle = TextStyle.Default, // 添加必要的樣式
            singleLine = true, // 確保文本框只有一行
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text // 指定鍵盤類型為文本
            )
        )
        TextField(
            value = productNumberText,
            onValueChange = { productNumberText = it },
            label = { Text("型號") },
            modifier = Modifier.fillMaxWidth(), // 添加必要的修飾符
            textStyle = TextStyle.Default, // 添加必要的樣式
            singleLine = true, // 確保文本框只有一行
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text // 指定鍵盤類型為文本
            )
        )
        TextField(
            value = productBatchText,
            onValueChange = { productBatchText = it },
            label = { Text("批號") },
            modifier = Modifier.fillMaxWidth(), // 添加必要的修飾符
            textStyle = TextStyle.Default, // 添加必要的樣式
            singleLine = true, // 確保文本框只有一行
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text // 指定鍵盤類型為文本
            )
        )
        TextField(
            value = producerNameText,
            onValueChange = { producerNameText = it },
            label = { Text("製造商/進口商") },
            modifier = Modifier.fillMaxWidth(), // 添加必要的修飾符
            textStyle = TextStyle.Default, // 添加必要的樣式
            singleLine = true, // 確保文本框只有一行
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text // 指定鍵盤類型為文本
            )
        )
        TextField(
            value = bsmiNumberText,
            onValueChange = { bsmiNumberText = it },
            label = { Text("商品檢驗標識") },
            modifier = Modifier.fillMaxWidth(), // 添加必要的修飾符
            textStyle = TextStyle.Default, // 添加必要的樣式
            singleLine = true, // 確保文本框只有一行
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text // 指定鍵盤類型為文本
            )
        )
        TextField(
            value = productSourceCountryText,
            onValueChange = { productSourceCountryText = it },
            label = { Text("國家") },
            modifier = Modifier.fillMaxWidth(), // 添加必要的修飾符
            textStyle = TextStyle.Default, // 添加必要的樣式
            singleLine = true, // 確保文本框只有一行
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text // 指定鍵盤類型為文本
            )
        )


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "原文: $resultText",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", resultText)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick ={
                // 处理拍照逻辑
//                            startCameraActivity(context)
                //文字辨識
//                val intent = Intent(context, ChooserActivity::class.java)
//                context.startActivity(intent)

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("提交")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick ={
                // 处理拍照逻辑
//                            startCameraActivity(context)
                //文字辨識
                val intent = Intent(context, ChooserActivity::class.java)
                context.startActivity(intent)

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("返回")
        }
    }
}



private fun extractProductName(resultText: String): String{
    val pattern = Pattern.compile("(品名)|(.*名.*)|(.*系.*)|(.*玩具.*)")
    val matcher = pattern.matcher(resultText)
    return if (matcher.find()) {
        // 匹配到了 "品名" 或 ".名"，则返回匹配到的文本
        matcher.group()
    } else {
        // 如果没有匹配到，则返回空字符串
        ""
    }
}

private fun extractproductNumber(resultText: String): String {
    val pattern = Pattern.compile("(型號)")
    val matcher = pattern.matcher(resultText)
    return if (matcher.find()) {
        // 匹配到了 "品名" 或 ".名"，则返回匹配到的文本
        matcher.group()
    } else {
        // 如果没有匹配到，则返回空字符串
        ""
    }
}

private fun extractproductBatch(resultText: String): String {
    val pattern = Pattern.compile(".*批號.*")
    val matcher = pattern.matcher(resultText)
    return if (matcher.find()) {
        // 匹配到，则返回匹配到的文本
        matcher.group()
    } else {
        // 如果没有匹配到，则返回空字符串
        ""
    }
}

private fun extractproducerName(resultText: String): String {
    val pattern = Pattern.compile("(進口商.*)|(.*製造商.*)|(.*製商.*)|(委製商.*)")
    val matcher = pattern.matcher(resultText)
    return if (matcher.find()) {
        // 匹配到了 "品名" 或 ".名"，则返回匹配到的文本
        matcher.group()
    } else {
        // 如果没有匹配到，则返回空字符串
        ""
    }
}

private fun extractbsmiNumber(resultText: String): String {
    val pattern = Pattern.compile("(M.*)|(R.*)|(T.*)|(D.*)")
    val matcher = pattern.matcher(resultText)
    return if (matcher.find()) {
        // 匹配到了 "品名" 或 ".名"，则返回匹配到的文本
        matcher.group()
    } else {
        // 如果没有匹配到，则返回空字符串
        ""
    }
}

private fun extractproductSourceCountry(resultText: String): String{
    val pattern = Pattern.compile("(中國)|(.*地:.*)|(中國大陸)|(台灣)|(臺灣)|(越南)|(柬埔寨)|(產地)")
    val matcher = pattern.matcher(resultText)
    return if (matcher.find()) {
        // 匹配到了 "品名" 或 ".名"，则返回匹配到的文本
        matcher.group()
    } else {
        // 如果没有匹配到，则返回空字符串
        ""
    }
}

//class NextActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // 问题1：检查是否成功获取 resultText
//        val resultText = intent.getStringExtra("resultText")
//        if (resultText == null) {
//            // 如果 resultText 为空，则显示错误信息并关闭当前页面
//            Toast.makeText(this, "No result text found", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        // 显示识别到的文字
//        setContent {
//            Surface {
//                ShowResultText(resultText) {
//                    // 当用户点击返回按钮时关闭当前页面
//                    finish()
//                }
//            }
//        }
//    }
//}

//class NextActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val resultText = intent.getStringExtra("resultText")
//        setContent {
//            Surface {
//                ShowResultText(resultText ?: "") {
//                    // 当用户点击返回按钮时关闭当前页面
//                    finish()
//                }
//            }
//        }
//    }
//}

//@Composable
//fun ShowResultText(resultText: String, onBackClick: () -> Unit) {
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
//        // 返回按钮
//        Button(
//            onClick = onBackClick, // 点击事件，调用传入的 onBackClick 函数
//            modifier = Modifier.padding(top = 16.dp)
//        ) {
//            Text("Back")
//        }
//    }
//}