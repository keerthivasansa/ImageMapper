package com.keerthivasan.imagemapper

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private fun showToast(text:String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun showSellers() {
        activityScope.launch {
            val sellers = SellerService("")
                .getSellers()
            val sellerAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, sellers)
        }
    }
}
