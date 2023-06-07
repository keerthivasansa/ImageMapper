package com.keerthivasan.imagemapper.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keerthivasan.imagemapper.ui.theme.ImageMapperTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(
    title: String,
    backButton: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    ImageMapperTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = FabPosition.End,
                topBar = {
                    if (backButton) {
                        TopBarWithBackBtn(title)
                    } else {
                        TopBar(title) {}
                    }
                }
            ) { padding ->
                Box(Modifier.padding(padding)) {
                    content()
                }
            }
        }
    }
}
