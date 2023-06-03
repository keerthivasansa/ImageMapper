package com.keerthivasan.imagemapper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class Utils {
    companion object {
        fun showToast(text:String, context: Context) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}