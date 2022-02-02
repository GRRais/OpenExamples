package ru.rayanis.issuedproducts.data

import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DataProvider {

    val issuedProductsRef = Firebase.firestore.collection("issuedProducts")

    private fun saveProducts(activity: ComponentActivity, product: Product) =
        CoroutineScope(Dispatchers.IO).launch {
        try {
            issuedProductsRef.add(product)
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, "Успешно сохранен", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}