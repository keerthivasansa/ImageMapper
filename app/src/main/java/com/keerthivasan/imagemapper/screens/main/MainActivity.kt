package com.keerthivasan.imagemapper.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keerthivasan.imagemapper.ui.theme.ImageMapperTheme
import com.keerthivasan.imagemapper.screens.createSeller.CreateSeller
import com.keerthivasan.imagemapper.SellerService
import com.keerthivasan.imagemapper.Utils
import com.keerthivasan.imagemapper.ui.core.AppShell

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppShell(title = "Sellers", floatingActionButton = { CreateSellerFAB() }) {
                SellerList()
            }
        }
    }
}

@Composable
fun CreateSellerFAB() {
    val context = LocalContext.current
    val switchIntent = Intent(context, CreateSeller::class.java)
    FloatingActionButton(
        onClick = {
            context.startActivity(switchIntent)
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(Icons.Default.Add, contentDescription = "Create Seller")
    }
}

@Composable
fun SellerList() {

    val sellerService = remember { SellerService("") }
    val (sellers, setSellers) = remember { mutableStateOf(listOf("Loading sellers . . .")) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val dbSellers = sellerService.getSellerNames()
        setSellers(dbSellers)
    }

    LazyColumn(modifier = Modifier.padding(top = 14.dp, start = 16.dp)) {
        items(sellers) { seller ->
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clickable {
                        val id = sellerService.getSellerId(seller)
                        coroutineScope.launch {
                            if (id is String)
                                SellerService(id).openSeller(context)
                            else
                                Utils.showToast("Failed to open $seller", context)
                        }
                    }, contentAlignment = Alignment.CenterStart
            ) {
                Text(text = seller, textAlign = TextAlign.Start, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ImageMapperTheme {
        SellerList()
    }
}