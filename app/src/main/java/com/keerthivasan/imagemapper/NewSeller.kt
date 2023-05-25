package com.keerthivasan.imagemapper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewSeller : AppCompatActivity() {
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_seller)
        val nameInput = findViewById<EditText>(R.id.seller_name_input)
        val numberInput = findViewById<EditText>(R.id.seller_number_input)
        val saveBtn = findViewById<Button>(R.id.save_btn)

        saveBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val number = numberInput.text.toString().toLong()
            activityScope.launch {
                SellerService("").createSeller(name, number, applicationContext)
                nameInput.setText("")
                numberInput.setText("")
            }
        }

    }
}