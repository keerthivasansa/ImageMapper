package com.keerthivasan.imagemapper

import android.content.Context
import android.widget.Toast

class Utils {
    companion object {
        fun showToast(text:String, context: Context) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}