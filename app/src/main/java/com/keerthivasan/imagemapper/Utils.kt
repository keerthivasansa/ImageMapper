package com.keerthivasan.imagemapper

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigInteger
import java.security.MessageDigest

class Utils {
    companion object {
        fun showToast(text:String, context: Context) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        fun md5(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }

        fun readStringFromUri(uri: Uri, contentResolver: ContentResolver): String? {
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
    }
}