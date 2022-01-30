package ru.rayanis.issuedproducts.data

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rayanis.issuedproducts.MainActivity
import java.lang.Exception

class DataProvider(val act: AppCompatActivity) {

    private val issuedProductsRef = Firebase.firestore.collection("issuedProducts")

    private fun saveProducts(product: Product) = CoroutineScope(Dispatchers.IO).launch {
        try {
            issuedProductsRef.add(product)
            withContext(Dispatchers.Main) {
                Toast.makeText(act, "Успешно сохранен", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(act, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}