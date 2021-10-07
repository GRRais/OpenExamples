package ru.rayanis.compocalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.rayanis.compocalculator.ui.theme.CompoCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompoCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn(content = {
                        item { TabloCalc("Android") }
                    })
                }
            }
        }
    }
}

@Composable
fun TabloCalc(name: String) {
    Box
    Text(
        text = "Hello $name!",
        modifier = Modifier.padding(start = 10.dp, top = 10.dp),
        fontSize = 30.sp
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CompoCalculatorTheme {
        TabloCalc("Android")
    }
}