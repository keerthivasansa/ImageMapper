package com.keerthivasan.imagemapper

import androidx.compose.foundation.shape.CornerSize
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(label:String, options: List<String>, currentItem: Item?, onChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val shape = if (expanded) RoundedCornerShape(8.dp).copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    else RoundedCornerShape(8.dp)

    var selectedOption = currentItem?.sellerId ?: ""
    if (selectedOption.isEmpty() && options.isNotEmpty())
        selectedOption = options.first()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        TextField(
            modifier = Modifier.menuAnchor(),
            textStyle = TextStyle.Default.copy(
                fontSize = 14.sp,
                fontWeight=  FontWeight.Light),
            readOnly = true,
            value = selectedOption,
            onValueChange = {},
            label = { Text(label, fontWeight = FontWeight.Bold, ) },
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
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onChange(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
