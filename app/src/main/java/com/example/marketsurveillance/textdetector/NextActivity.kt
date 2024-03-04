package com.example.marketsurveillance.textdetector

//import androidx.compose.ui.platform.ClipboardManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

        val scrollState = rememberScrollState() // 定义滚动状态

        setContent {
            Surface {
                VerticalScrollbar(
                    modifier = Modifier.fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState)
                ) {
                    Column(
                        modifier = Modifier.verticalScroll(scrollState)
                    ) {
                        ShowResultText(
                            productName = productNameText,
                            productNumber = productNumberText,
                            productBatch = productBatchText,
                            producerName = producerNameText,
                            bsmiNumber = bsmiNumberText,
                            productSourceCountry = productSourceCountryText,
                            resultText = resultText ?: ""
                        ) {
                            finish()
                        }
                    }
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
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "品名: $productName",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", productName)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "型號: $productNumber",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", productNumber)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "批號: $productBatch",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", productBatch)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "製造商/進口商: $producerName",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", producerName)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "商品檢驗標識: $bsmiNumber",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", bsmiNumber)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "國家: $productSourceCountry",
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.clickable {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", productSourceCountry)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }
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
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("返回")
        }
    }
}



private fun extractProductName(resultText: String): String {
    // 这里可以编写您的逻辑来提取品名相关的文本，这里只是一个示例
    // 假设品名相关的文本位于 resultText 中的第一行
    val lines = resultText.split("\n")
    return if (lines.isNotEmpty()) {
        lines[0]
    } else {
        ""
    }
}

private fun extractproductNumber(resultText: String): String {
    val pattern = Pattern.compile("(品名)|(.名)")
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
    // 这里可以编写您的逻辑来提取品名相关的文本，这里只是一个示例
    // 假设品名相关的文本位于 resultText 中的第一行
    val lines = resultText.split("\n")
    return if (lines.isNotEmpty()) {
        lines[1]
    } else {
        ""
    }
}

private fun extractproducerName(resultText: String): String {
    // 这里可以编写您的逻辑来提取品名相关的文本，这里只是一个示例
    // 假设品名相关的文本位于 resultText 中的第一行
    val lines = resultText.split("\n")
    return if (lines.isNotEmpty()) {
        lines[2]
    } else {
        ""
    }
}

private fun extractbsmiNumber(resultText: String): String {
    // 这里可以编写您的逻辑来提取品名相关的文本，这里只是一个示例
    // 假设品名相关的文本位于 resultText 中的第一行
    val lines = resultText.split("\n")
    return if (lines.isNotEmpty()) {
        lines[3]
    } else {
        ""
    }
}

private fun extractproductSourceCountry(resultText: String): String {
    // 这里可以编写您的逻辑来提取品名相关的文本，这里只是一个示例
    // 假设品名相关的文本位于 resultText 中的第一行
    val lines = resultText.split("\n")
    return if (lines.isNotEmpty()) {
        lines[4]
    } else {
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