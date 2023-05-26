package com.keerthivasan.imagemapper

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger
import java.security.MessageDigest

class ImageIntent : AppCompatActivity() {
    private fun showToast(text:String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    private val db = Firebase.firestore
    private val collectionRef = db.collection("images")
    private val sellerService = SellerService("")
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_intent)
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                intent.type?.let{
                    if (it.startsWith("image/"))
                        handleImage()
                }
            }
            else -> showToast("Started with another intent")
        }
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun readStringFromUri(uri: Uri): String? {
        val stringBuilder = StringBuilder()
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return stringBuilder.toString()
    }

    private suspend fun updateDetails(docId: String) {
        val messageText = intent.getStringExtra(Intent.EXTRA_TEXT)
        val descInput = findViewById<EditText>(R.id.url_input)
        val doc = collectionRef.document(docId).get().await()
        val docExists = doc != null
        if (!docExists) {
            showToast("Creating new record")
            if (messageText != null)
                descInput.setText(messageText)
            return
        }
        val desc = doc.getString("description")
        descInput.setText(desc)
        val sellerId = doc.getString("seller")
        val sellerDetails = findViewById<Spinner>(R.id.seller_select)
        val sellers = sellerService.getSellerNames()
        sellerDetails.adapter = ArrayAdapter(applicationContext, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sellers)
        if (sellerId != null) {
            val seller = SellerService(sellerId)
            val data = seller.get()
            val sellerName = data.name
            val index = sellers.indexOf(sellerName)
            sellerDetails.setSelection(index)
        }
        val openBtn = findViewById<Button>(R.id.open_btn)

        openBtn.setOnClickListener {
            val activeSellerName = sellers[sellerDetails.selectedItemPosition]
            val id = sellerService.getSellerId(activeSellerName) ?: throw Error("Failed to get current seller id")
            activityScope.launch {
                SellerService(id).openSeller(applicationContext)
            }
        }
    }

    private fun handleImage() {
        val imagePreview = findViewById<ImageView>(R.id.image_preview)
        val descInput = findViewById<EditText>(R.id.url_input)
        val sellerInput = findViewById<Spinner>(R.id.seller_select)
        val item = intent.clipData?.getItemAt(0)
        val saveBtn = findViewById<Button>(R.id.save_btn)
        item?.uri?.let {
            val data = readStringFromUri(it) ?: return@let
            val hash = md5(data)

            imagePreview.setImageURI(it)

            saveBtn.setOnClickListener {
                val desc = descInput.text.toString()
                val sellerName = sellerInput.selectedItem.toString()
                val sellerId = sellerService.getSellerId(sellerName)
                val newData = hashMapOf("description" to desc, "seller" to sellerId)
                collectionRef.document(hash).set(newData)
                    .addOnSuccessListener {
                    showToast("Saved to database!")
                }
            }

            activityScope.launch {
                updateDetails(hash)
            }
        }
    }
}