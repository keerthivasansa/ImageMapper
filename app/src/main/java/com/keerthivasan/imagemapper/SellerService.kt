package com.keerthivasan.imagemapper

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class Seller(val name: String, val number: Long, val id: String)


class SellerService(private val sellerId: String) {

    private val db = Firebase.firestore
    private val collectionRef = db.collection("sellers")
    private var data: Seller? = null
    private val sellerMap: MutableMap<String, String> = mutableMapOf()

    suspend fun getSellers(): List<String> {
        val docs = collectionRef.get().await() ?: return listOf()
        sellerMap.clear()
        val sellerNames = docs.documents.map {doc ->
            val seller = getSellerFromDoc(doc)
            sellerMap[seller.name] = seller.id
            seller.name
        }
        return sellerNames
    }

    private fun getSellerFromDoc(doc: DocumentSnapshot?): Seller {
        val name = doc?.getString("name")
        val number = doc?.getLong("number")

        if (name != null && number != null) {
            data = Seller(name, number, doc.id)
            return data as Seller
        }
        throw Error("Missing name, number parameters")
    }

    suspend fun get(): Seller {
        if (data != null)
            return data as Seller;
        val doc = collectionRef.document(sellerId).get().await()
        return getSellerFromDoc(doc)
    }

    fun openSeller(ctx: Context) {
        if (data == null)
            throw Error("Called openSeller before get")
        val phoneNumber = data!!.number
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
        intent.setPackage("com.whatsapp")
        ctx.startActivity(intent)
    }

}