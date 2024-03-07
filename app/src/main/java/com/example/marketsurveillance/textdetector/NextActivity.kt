package com.example.marketsurveillance.textdetector

//import androidx.compose.ui.platform.ClipboardManager
//傳送資料到google sheets
//上傳資料到google sheet
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.marketsurveillance.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.regex.Pattern

//姓名選擇器
//@Composable
//fun NamePicker() {
//    val nameOptions = listOf("李O昌", "蔡O成", "許O進", "周O瑜", "梁O婷", "潘O婷")
//    var selectedName by remember { mutableStateOf(nameOptions[0]) }
//    var expandedName by remember { mutableStateOf(false) }
//    val density = LocalDensity.current
//    val dp = with(density) { 16.toDp() }
//
//
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        Text(
//            text = "檢查人員:",
//            modifier = Modifier.padding(dp)
//        )
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .clickable(onClick = { expandedName = true })
//                .background(Color.Gray)
//        ) {
//            Text(
//                text = selectedName,
//                modifier = Modifier.padding(dp)
//            )
//            DropdownMenu(
//                expanded = expandedName,
//                onDismissRequest = { expandedName = false }
//            ) {
//                nameOptions.map { name ->
//                    DropdownMenuItem(
//                        onClick = {
//                            selectedName = name
//                            expandedName = false
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        text = { Text(text = name) }
//                    )
//
//                }
//            }
//        }
//    }
//}

@Composable
fun NamePicker(onNameSelected: (String) -> Unit) {
    val nameOptions = listOf("請選擇人員","李O昌", "蔡O成", "許O進", "周O瑜", "梁O婷", "潘O婷")
    var selectedName by remember { mutableStateOf(nameOptions[0]) }
    var expandedName by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val dp = with(density) { 16.toDp() }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "檢查人員:",
            modifier = Modifier.padding(dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = { expandedName = true })
                .background(Color.Gray)
        ) {
            Text(
                text = selectedName,
                modifier = Modifier.padding(dp)
            )
            DropdownMenu(
                expanded = expandedName,
                onDismissRequest = { expandedName = false }
            ) {
                nameOptions.map { name ->
                    DropdownMenuItem(
                        onClick = {
                            selectedName = name
                            expandedName = false
                            onNameSelected(name)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = { Text(text = name) }
                    )
                }
            }
        }
    }
}


//本頁面的參數
class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultText = intent.getStringExtra("resultText")

        val allText = extractallText(resultText ?: "")
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
                            allwords = allText,
                            namepicker = "", // 提供空字串
                            specdate = "",
                            context = context
                        ) {
                            finish()
                        }
                    }
                }
            }
        }

@Composable
//欄位呈現的樣貌
fun ShowResultText(
    productName: String,
    productNumber: String,
    productBatch: String,
    producerName: String,
    bsmiNumber: String,
    productSourceCountry: String,
    allwords: String,
    namepicker: String,
    specdate: String,
    context: Context,
    onBackClick: () -> Unit
) {
    var productNameText by remember { mutableStateOf<String>("") }
    var productNumberText by remember { mutableStateOf<String>("") }
    var productBatchText by remember { mutableStateOf<String>("") }
    var producerNameText by remember { mutableStateOf<String>("") }
    var bsmiNumberText by remember { mutableStateOf<String>("") }
    var productSourceCountryText by remember { mutableStateOf<String>("") }
    var specdate by remember { mutableStateOf(TextFieldValue()) }
    var namePickerSelection by remember { mutableStateOf("") }
    var allText by remember { mutableStateOf(allwords) }


    //送出成功
    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = scaffoldState.snackbarHostState

//部屬頁面會給url
    fun sendDataToGoogleSheets() {
        val url = "https://script.google.com/macros/s/000000000/exec"

//        val formattedResultText = resultText.replace("\n", " ") //"\n", "\\n"無法解決原文沒有上傳sheet的問題
        val params = JSONObject().apply {
            put("source", "NextActivity")
            put("productName", productNameText)
            put("productNumber", productNumberText)
            put("productBatch", productBatchText)
            put("producerName", producerNameText)
            put("bsmiNumber", bsmiNumberText)
            put("productSourceCountry", productSourceCountryText)
            put("specDate", specdate.text)
            put("namepicker", namePickerSelection)
            put("allwords", allText)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            { response ->
                // Handle success response
                // You can update UI or show a success message here

            },
            { error ->
                // Handle error response
                // You can show an error message or log the error here
            }
        )
        request.setShouldCache(false)
        // Add the request to the RequestQueue
        val queue: RequestQueue = Volley.newRequestQueue(context)
        queue.add(request)

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            snackbarHostState.showSnackbar("送出成功")
        }
    }



    LaunchedEffect(true) {
        productNameText = productName
        productNumberText = productNumber
        productBatchText = productBatch
        producerNameText = producerName
        bsmiNumberText = bsmiNumber
        productSourceCountryText = productSourceCountry
        val formattedAllText = allwords.replace("\n", " ")
        allText = formattedAllText

    }
    Scaffold(
        scaffoldState = scaffoldState,
        content = { contentPadding ->
            // 這裡是您原有的 Column 內容
// 包裹 Column 在 ScrollView 中

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
                    .padding(16.dp)
            ) {
                TextField(
                    value = specdate,
                    onValueChange = { specdate = it },
                    label = { Text("檢查日期(Ex:1130101)") }
                )
                NamePicker(
                    onNameSelected = { selectedName ->
                        namePickerSelection = selectedName
                    }

                )
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

//原文:對使用者隱藏，但會送到EXCEL
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "原文: $resultText",
//                fontSize = 16.sp,
//                textAlign = TextAlign.Start,
//                modifier = Modifier.clickable {
//                    val clipboard =
//                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                    val clip = ClipData.newPlainText("Copied Text", resultText)
//                    clipboard.setPrimaryClip(clip)
//                    Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
//                }
//            )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {

                        sendDataToGoogleSheets()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("提交")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {

                        //返回文字辨識選擇器
                        val intent = Intent(context, ChooserActivity::class.java)
                        context.startActivity(intent)

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("返回上一頁")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {

                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("返回首頁")
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // 添加 SnackbarHost

    )
}





//設計文字篩選功能
private fun extractallText(resultText: String): String {
    return resultText ?: "Error: resultText is null"
}
private fun extractProductName(resultText: String): String{
    val pattern = Pattern.compile("(.*品名.*\\S)|(.*名.*\\S)|(.*系.*)|(.*玩具.*)")
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
    val pattern = Pattern.compile("(.*型號.*)")
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

//按權重設定篩選條件的順序
private fun extractproducerName(resultText: String): String {
    val pattern = Pattern.compile("(.*進口商.*\\S)|(.*製造商.*\\S)|(.*製商.*\\S)|(.*委製商.*\\S)")
    val matcher = pattern.matcher(resultText)

    var bestMatch = ""
    var foundImporter = false

    while (matcher.find()) {
        val match = matcher.group()
        if (match.startsWith("進口商") && !foundImporter) {
            bestMatch = match
            foundImporter = true
            break // 找到進口商就退出循環
        } else if (match.contains("委製商") && !foundImporter) {
            bestMatch = match
        } else if (match.contains("委製商") && foundImporter && !bestMatch.contains("委製商")) {
            bestMatch = match
        }
        else if (match.contains("製造商") && !foundImporter) {
            bestMatch = match
        } else if (match.contains("製造商") && foundImporter && !bestMatch.contains("製造商")) {
            bestMatch = match
        } else if (match.contains("製商") && !foundImporter) {
            bestMatch = match
        } else if (match.contains("製商") && foundImporter && !bestMatch.contains("製商")) {
            bestMatch = match
        }
    }

    return bestMatch
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
    val pattern = Pattern.compile("(中國)|(.*地:.*\\S)|(中國大陸)|(台灣)|(臺灣)|(越南)|(柬埔寨)|(產地:.*\\S)")
    val matcher = pattern.matcher(resultText)
    return if (matcher.find()) {
        // 匹配到了 "品名" 或 ".名"，则返回匹配到的文本
        matcher.group()
    } else {
        // 如果没有匹配到，则返回空字符串
        ""
    }
}
//@Composable
//fun NamePicker(
//    nameOptions: List<String>,
//    onNameSelected: (String) -> Unit
//) {
//    // ... 其他邏輯 ...
//
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        // ... 其他邏輯 ...
//
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .clickable(onClick = { expandedName = true })
//                .background(Color.Gray)
//        ) {
//            // ... 其他邏輯 ...
//
//            DropdownMenu(
//                expanded = expandedName,
//                onDismissRequest = { expandedName = false }
//            ) {
//                nameOptions.map { name ->
//                    DropdownMenuItem(
//                        onClick = {
//                            onNameSelected(name)
//                            expandedName = false
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        text = { Text(text = name) }
//                    )
//                }
//            }
//        }
//    }
//}
//串接google sheet

//https://www.youtube.com/watch?v=ab3ngR0Niic
//https://google.github.io/volley/
