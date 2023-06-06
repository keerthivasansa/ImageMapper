@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.keerthivasan.imagemapper

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.keerthivasan.imagemapper.ui.theme.ImageMapperTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.DateFormat
import java.util.Date

data class Item(
    val imageUri: Uri,
    var description: String,
    val sellerId: String,
    val date: Date,
)

class ItemIntent : ComponentActivity() {

    private val db = Firebase.firestore
    private val collectionRef = db.collection("images")
    private val sellerService = SellerService("")
    private var imageHash = ""
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ImageMapperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var currentItem by remember { mutableStateOf<Item?>(null) }
                    var sellers by remember { mutableStateOf<List<Seller>>(listOf()) }
                    val scrollState = rememberScrollState()

                    LaunchedEffect(Unit) {
                        val item = getItem()
                        if (item == null)
                            Toast.makeText(applicationContext, "Item null", Toast.LENGTH_SHORT)
                                .show()
                        currentItem = item
                        sellers = sellerService.getSellers()
                        if (item?.sellerId == "" && sellers.isNotEmpty())
                            currentItem = item.copy(sellerId = sellers.first().id)
                    }

                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        ),
                    ) {
                        ItemScreen(currentItem, onDescriptionChange = {
                            val newItem = currentItem?.copy(description = it)
                            currentItem = newItem
                        })
                        Dropdown(
                            "Seller:",
                            sellers,
                            currentItem,
                            onChange = { currentItem = currentItem?.copy(sellerId = it) })
                        CalendarDialog(currentItem)
                        Row {
                            Button(onClick = { saveItem(currentItem) }) {
                                Text("Save")
                            }
                            Button(
                                onClick = { openSellerChat(currentItem) },
                                Modifier.padding(start = 24.dp)
                            ) {
                                Text("Open")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openSellerChat(currentItem: Item?) {
        if (currentItem == null)
            return
        val seller = SellerService(currentItem.sellerId)
        coroutineScope.launch {
            seller.get()
            seller.openSellerWithImage(applicationContext, currentItem.imageUri)
        }
    }

    private fun saveItem(currentItem: Item?) {
        if (currentItem == null) {
            Toast.makeText(
                applicationContext,
                "Item not initialized yet, please wait.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val data = hashMapOf(
            "description" to currentItem.description,
            "created_at" to currentItem.date,
            "seller" to currentItem.sellerId
        )

        collectionRef
            .document(imageHash)
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Saved to Database", Toast.LENGTH_SHORT).show()
            }
    }

    private suspend fun getItem(): Item? {
        val item = intent.clipData?.getItemAt(0)
        item?.uri?.let { imageUri ->
            val data = Utils.readStringFromUri(imageUri, contentResolver) ?: return@let
            imageHash = Utils.md5(data)
            val doc = collectionRef.document(imageHash).get().await()
            val desc = doc.getString("description")
            val sellerId = doc.getString("seller")
            val createdAt = doc.getDate("created_at")
            if (desc == null || sellerId == null || createdAt == null) {
                println("Item is null. Description: $desc, SellerId: $sellerId")
                return Item(imageUri, "", "", Date())
            }
            return Item(imageUri, desc, sellerId, createdAt)
        }
        println("Item is null. No imageUri found.")
        return null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(currentItem: Item?) {
    val shape = RoundedCornerShape(8.dp)
    val formatter = DateFormat.getDateTimeInstance()

    TextField(
        textStyle = TextStyle.Default.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        ),
        readOnly = true,
        value = formatter.format(currentItem?.date ?: Date()),
        onValueChange = {},
        label = { Text("Created on:", fontWeight = FontWeight.ExtraBold ) },
        trailingIcon = {
            Icon(
                painterResource(com.google.android.material.R.drawable.material_ic_calendar_black_24dp),
                contentDescription = "Calendar"
            )
        },
        shape = shape,
        colors = ExposedDropdownMenuDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    currentItem: Item?,
    onDescriptionChange: (String) -> Unit
) {
    if (currentItem != null) {
        Image(
            painter = rememberAsyncImagePainter(currentItem.imageUri),
            contentDescription = "Item Image",
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        )
        TextField(value = currentItem.description, onValueChange = onDescriptionChange)
    } else {
        Text("Loading image", modifier = Modifier.padding(bottom = 24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    ImageMapperTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val uri =
                Uri.parse("https://assets.myntassets.com/h_1440,q_90,w_1080/v1/assets/images/19473744/2022/9/13/5b4fc687-b93b-4141-aeb6-1bd82db75e671663054576612-Antheaa-Women-Dresses-631663054576038-1.jpg")
            val currentItem = Item(uri, "nice", "sef", Date())
            ItemScreen(currentItem, onDescriptionChange = {})
        }
    }
}