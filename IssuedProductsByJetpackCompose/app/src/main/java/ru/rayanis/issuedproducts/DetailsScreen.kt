package ru.rayanis.issuedproducts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.rayanis.issuedproducts.data.Product

@Composable
fun DetailsScreen(
    navController: NavController,
    product: Product
) {

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
//        DetailsProperty(stringResource(R.string.date) , product.date)
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
        TextField(
            value = product.title,
            label = {
                Text("Название изделия")
            },
            onValueChange = {
                it
            })
    }
}

@Composable
private fun DetailsProperty(label: String, value: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = value,
            label = {
                Text(text = label)},
            onValueChange = {
                it
            })
    }
}