package com.keerthivasan.imagemapper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keerthivasan.imagemapper.ui.theme.ImageMapperTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keerthivasan.imagemapper.ui.theme.IndigoPrimary

class MainActivity2 : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageMapperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        floatingActionButton = { CreateSellerFAB() },
                        floatingActionButtonPosition = FabPosition.End,
                        topBar = { AppBar() }
                    ) { padding ->
                        SellerList(padding)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(title = { Text("Seller")},
    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = IndigoPrimary, titleContentColor = Color.White))
}

@Composable
fun CreateSellerFAB() {
    val context = LocalContext.current
    val switchIntent = Intent(context, CreateSeller::class.java)
    FloatingActionButton(onClick = {
        context.startActivity(switchIntent)
    }) {
        Icon(Icons.Default.Add, contentDescription = "Create Seller")
    }
}

@Composable
fun SellerList(padding: PaddingValues) {

    val sellerService = remember { SellerService("") }
    val (sellers, setSellers)  = remember { mutableStateOf(listOf("Loading sellers . . .")) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val dbSellers = sellerService.getSellerNames()
        setSellers(dbSellers)
    }

    LazyColumn(modifier = Modifier.padding(top = padding.calculateTopPadding() + 14.dp, start = 16.dp)) {
        items(sellers) { seller ->
            Box(modifier = Modifier.height(60.dp).width(120.dp).clickable {
                val id = sellerService.getSellerId(seller)
                coroutineScope.launch {
                    if (id is String)
                        SellerService(id).openSeller(context)
                    else
                        Utils.showToast("Failed to open $seller", context)
                }
            }, contentAlignment = Alignment.CenterStart) {
                Text(text = seller, textAlign = TextAlign.Start, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ImageMapperTheme {
        SellerList(padding = PaddingValues(all=12.dp))
    }
}