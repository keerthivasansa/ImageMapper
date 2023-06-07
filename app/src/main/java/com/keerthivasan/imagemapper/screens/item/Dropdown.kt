package com.keerthivasan.imagemapper.screens.item

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keerthivasan.imagemapper.Seller

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(label:String, options: List<Seller>, currentItem: Item?, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(8.dp)

    var selectedOption = currentItem?.sellerId ?: ""
    if (selectedOption.isEmpty() && options.isNotEmpty())
        selectedOption = options.first().id

    val currentSellerName = options.find { seller -> seller.id == selectedOption }?.name ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        TextField(
            modifier = Modifier.menuAnchor(),
            textStyle = TextStyle.Default.copy(
                fontSize = 15.sp,
                fontWeight=  FontWeight.Bold),
            readOnly = true,
            value = currentSellerName,
            onValueChange = {},
            label = { Text(label, fontWeight = FontWeight.ExtraBold ) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = shape,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { seller ->
                DropdownMenuItem(
                    text = { Text(seller.name) },
                    onClick = {
                        onChange(seller.id)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
