package ru.rayanis.issuedproducts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.rayanis.issuedproducts.data.Product

@Composable
fun ProductListItem(product: Product, navigateToDetails: (Product) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp , vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp ,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(Modifier.clickable{navigateToDetails(product)}) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = product.title, style = MaterialTheme.typography.h6)
                Text(text = product.destination, style = MaterialTheme.typography.h6)
                Text(text = product.productCost.toString(), style = MaterialTheme.typography.h6)
                Text(text = product.description, style = MaterialTheme.typography.h6)
            }
        }
    }
}