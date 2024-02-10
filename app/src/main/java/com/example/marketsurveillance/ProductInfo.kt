package com.example.marketsurveillance


//import androidx.compose.material.Checkbox 因為不合格的還是用紙本，不用勾選



//設定日期
//等調好日期選擇器的長相再用
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketCheckScreen() {
    var productName by remember { mutableStateOf(TextFieldValue()) }
    var productNumber by remember { mutableStateOf(TextFieldValue()) }
    var productSpec by remember { mutableStateOf(TextFieldValue()) }
    var productBrand by remember { mutableStateOf(TextFieldValue()) }
    var productDate by remember { mutableStateOf(TextFieldValue()) }
    var productBatch by remember { mutableStateOf(TextFieldValue()) }
    var producerName by remember { mutableStateOf(TextFieldValue()) }
    var producerAddress by remember { mutableStateOf(TextFieldValue()) }
    var productSourceCountry by remember { mutableStateOf(TextFieldValue()) }
    var bsmiNumber by remember { mutableStateOf(TextFieldValue()) }




//設定時間
//    val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val datePickerState = rememberDatePickerState()
    var specDate by remember { mutableStateOf(TextFieldValue()) }




    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
            Text(text = "請輸入合格的商品資訊", fontSize = 30.sp)}
            // 檢查日期選擇器
            item {
            TextField(
                value = specDate,
                onValueChange = { specDate = it },
                label = { Text("檢查日期(Ex:1130101)") }
            )}
            // 檢查日期選擇器，在很突兀的位置，待修改
//            item {TextField(
//                value = specDate,
//                onValueChange = { specDate = it },
//                label = { Text(text = "檢查日期") },
//                modifier = Modifier.fillMaxWidth(),
//                trailingIcon = {
//                    ComposeDatePicker(
//                        state = datePickerState,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//            )}
            // 人名選擇器
            item {NamePicker()}
            //商品資訊
            item {TextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("商品名稱") }
            )}
            item {TextField(
                value = productNumber,
                onValueChange = { productNumber = it },
                label = { Text("型號") }
            )}
            item {TextField(
                value = productSpec,
                onValueChange = { productSpec = it },
                label = { Text("規格") }
            )}
            item {TextField(
                value = productBrand,
                onValueChange = { productBrand = it },
                label = { Text("廠牌") }
            )}
            item {TextField(
                value = productDate,
                onValueChange = { productDate = it },
                label = { Text("製造日期") }
            )}
            item {TextField(
                value = productBatch,
                onValueChange = { productBatch = it },
                label = { Text("批號") }
            )}
            item {TextField(
                value = producerName,
                onValueChange = { producerName = it },
                label = { Text("產製者或輸入者名稱") }
            )}
            item {TextField(
                value = producerAddress,
                onValueChange = { producerAddress = it },
                label = { Text("產製者或輸入者地址") }
            )}
            item {TextField(
                value = productSourceCountry,
                onValueChange = { productSourceCountry = it },
                label = { Text("生產國別") }
            )}
            item {TextField(
                value = bsmiNumber,
                onValueChange = { bsmiNumber = it },
                label = { Text("商品檢驗標識識別號碼") }
            )}
//日後可以改成合格不合格的判定
//            Checkbox(
//                checked = isAvailable,
//                onCheckedChange = { isAvailable = it },
//                modifier = Modifier.padding(vertical = 8.dp)
//            ) {
//                Text(text = "商品是否可用")
//            }
            item {
                    Button(
                        onClick = {
                            // 在此处处理提交商品信息的逻辑
                        },
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Text(text = "提交")}
            }
        }
    }
}


//人名選單
@Composable
fun NamePicker() {
    val nameOptions = listOf("李O昌", "蔡O成", "許O進", "周O瑜","梁O婷", "潘O婷")
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
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = { Text(text = name) }
                    )

                    }
                }
            }
        }
    }

