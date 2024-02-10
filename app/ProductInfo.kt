import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarketCheckScreen() {
    var productName by remember { mutableStateOf(TextFieldValue()) }
    var productPrice by remember { mutableStateOf(TextFieldValue()) }
    var productDescription by remember { mutableStateOf(TextFieldValue()) }
    var isAvailable by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "請輸入商品資訊", fontSize = 30.sp)
            TextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("商品名稱") }
            )
            TextField(
                value = productPrice,
                onValueChange = { productPrice = it },
                label = { Text("價格") }
            )
            TextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("描述") }
            )
            Checkbox(
                checked = isAvailable,
                onCheckedChange = { isAvailable = it },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "商品是否可用")
            }
            Button(
                onClick = {
                    // 在此处处理提交商品信息的逻辑
                },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(text = "提交")
            }
        }
    }
}