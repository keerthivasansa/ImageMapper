package com.keerthivasan.imagemapper.ui.core

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.keerthivasan.imagemapper.screens.main.MainActivity2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, navigationIcon: @Composable () -> Unit) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
fun TopBarWithBackBtn(title: String) {
    val context = LocalContext.current
    TopBar(title = title, navigationIcon = {
        IconButton(onClick = {
            val intent = Intent(context, MainActivity2::class.java)
            context.startActivity(intent)
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
        }
    })
}