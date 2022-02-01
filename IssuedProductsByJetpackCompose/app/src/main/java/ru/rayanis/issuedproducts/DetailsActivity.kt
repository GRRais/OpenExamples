package ru.rayanis.issuedproducts

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import ru.rayanis.issuedproducts.data.Product
import ru.rayanis.issuedproducts.ui.theme.IssuedProductsTheme

class DetailsActivity : ComponentActivity() {

    private val product: Product by lazy {
        intent?.getSerializableExtra(PRODUCT_ID) as Product
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) {
                IssuedProductsTheme {
                    DetailsScreen(product = product)
                }
            }
        }
    }

    companion object {
        private const val PRODUCT_ID = "product_id"
        fun newIntent(context: Context , product: Product) =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(PRODUCT_ID, product)
            }
    }
}