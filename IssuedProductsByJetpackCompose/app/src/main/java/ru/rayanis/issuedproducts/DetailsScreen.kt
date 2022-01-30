package ru.rayanis.issuedproducts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.rayanis.issuedproducts.data.Product

@Composable
fun DetailsScreen(product: Product) {

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    DetailsContent(product)

                }
            }
        }
    }
}

@Composable
private fun DetailsContent(
    product: Product
) {
    Column() {
        Title(product)
        DetailsProperty(stringResource(R.string.title) , product.title)
        DetailsProperty(stringResource(R.string.destination) , product.destination)
        DetailsProperty(stringResource(R.string.date) , product.date.toString())
        DetailsProperty(stringResource(R.string.quantity) , product.quantity.toString())
        DetailsProperty(stringResource(R.string.productCost) , product.productCost.toString())
        DetailsProperty(stringResource(R.string.description) , product.description)
        DetailsProperty(stringResource(R.string.quantPersons) , product.quantPersons.toString())
        Divider(modifier = Modifier.padding(bottom = 4.dp))
    }

}

@Composable
private fun Title(
    product: Product
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = product.title,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DetailsProperty(label: String, value: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = label,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.caption,
        )
        Text(
            text = value,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Visible
        )
    }
}