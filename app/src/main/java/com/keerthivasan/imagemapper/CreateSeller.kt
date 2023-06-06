package com.keerthivasan.imagemapper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keerthivasan.imagemapper.ui.theme.ImageMapperTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import com.keerthivasan.imagemapper.ui.theme.IndigoPrimary


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
        Scaffold(topBar = { SellerTopBar() }) { paddingValues ->
            Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding() + 12.dp, start = 16.dp)) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerTopBar() {
    val context = LocalContext.current
    TopAppBar(title = { Text("Create new Seller", fontWeight = FontWeight.Bold) },
        navigationIcon = {
                         IconButton(onClick = {
                             val intent = Intent(context, MainActivity2::class.java)
                             context.startActivity(intent)
                         }) {
                             Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back", tint = Color.Black)
                         }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageMapperTheme {
        SellerScreen()
    }
}