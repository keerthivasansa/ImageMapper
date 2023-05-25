package com.keerthivasan.imagemapper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showSellers()
        val createBtn = findViewById<FloatingActionButton>(R.id.create_btn)
        createBtn.setOnClickListener {
            val switch = Intent(this@MainActivity, NewSeller::class.java)
            startActivity(switch)
        }
    }

    private fun showSellers() {
        val sellersService = SellerService("")
        activityScope.launch {
            val sellerList = findViewById<ListView>(R.id.seller_list)
            val sellers = sellersService
                .getSellers()
            val sellerAdapter = ArrayAdapter(applicationContext, R.layout.seller_listview, sellers)
            sellerList.adapter = sellerAdapter

            sellerList.setOnItemClickListener { _, _, position, _ ->
                val sellerName = sellers[position]
                val sellerId = sellersService.getSellerId(sellerName)
                if (sellerId != null) {
                    activityScope.launch {
                        SellerService(sellerId).openSeller(applicationContext)
                    }
                }
                else
                    Utils.showToast("Seller does not exist on the map", applicationContext)
            }
        }
    }
}
