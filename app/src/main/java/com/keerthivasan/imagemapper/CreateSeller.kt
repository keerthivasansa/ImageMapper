package com.keerthivasan.imagemapper

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keerthivasan.imagemapper.ui.theme.ImageMapperTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly


class CreateSeller : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageMapperTheme {
                // A surface container using the 'background' color from the theme
                SellerScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerScreen() {
    var name by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun showInvalidNumberToast() {
        Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Seller name:")
            TextField(name, { name = it }, modifier= Modifier.padding(top=12.dp))
            Text(text = "Seller number: ", modifier= Modifier.padding(top=20.dp))
            TextField(value = numberInput, { numberInput = it }, modifier=Modifier.padding(top=12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Button(onClick = {
                try {
                    if (numberInput.isEmpty() || !numberInput.isDigitsOnly() && numberInput.length != 10) {
                        showInvalidNumberToast()
                        return@Button
                    }
                    val number = numberInput.toLong()
                    SellerService("").createSeller(name, number, context)
                    name = ""
                    numberInput = ""
                } catch (e: NumberFormatException) {
                    showInvalidNumberToast()
                }
            }, modifier = Modifier.padding(top=24.dp)) {
                Text(text = "Add Seller")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageMapperTheme {
        SellerScreen()
    }
}