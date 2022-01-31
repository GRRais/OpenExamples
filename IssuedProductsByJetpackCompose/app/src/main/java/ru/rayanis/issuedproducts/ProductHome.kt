package ru.rayanis.issuedproducts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import ru.rayanis.issuedproducts.data.DataProvider
import ru.rayanis.issuedproducts.data.Product

@Composable
fun ProductHomeContent(navigateToDetails: (Product) -> Unit) {
    val products = remember{DataProvider.issuedProductsRef}
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = products,
            itemContent = {
                ProductListItem(product = it, navigateToDetails)
            }
            )
    }
}