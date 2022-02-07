package ru.rayanis.issuedproducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.rayanis.issuedproducts.data.Product
import ru.rayanis.issuedproducts.ui.theme.IssuedProductsTheme

class MainActivity : ComponentActivity() {

    val issuedProductsRef = Firebase.firestore.collection("issuedProducts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IssuedProductsTheme {
                val navController = rememberNavController()
                val product: Product
                NavHost(
                    navController = navController ,
                    startDestination = "productsScreen"
                ) {
                    composable("productsScreen") {
                        ProductsScreen(navController)
                    }
                    composable(
                        route = "details/{title}/{}/{}/{}/{}/{}" +
                                "/{}/{}/{}",
                        )
                    {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun ProductsScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(end = 8.dp , bottom = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,
    ) {
        FloatingActionButton(onClick = {
            navController.navigate("profile")
        },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        )
    }
}

@Composable
fun ProfileScreen() {
    Column() {
        Title()
        DetailsProperty(stringResource(R.string.title))
        DetailsProperty(stringResource(R.string.destination))
//        DetailsProperty(stringResource(R.string.date) , product.date)
        DetailsProperty(stringResource(R.string.quantity))
        DetailsProperty(stringResource(R.string.productCost))
        DetailsProperty(stringResource(R.string.description))
        DetailsProperty(stringResource(R.string.quantPersons))
        Divider(modifier = Modifier.padding(bottom = 4.dp))

    }
}

@Composable
private fun Title(
//    product: Product
) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = "product.title",
            label = {
                Text("Название изделия")
            },
            onValueChange = {
                it
            })
    }
}

@Composable
private fun DetailsProperty(label: String, value: String = "") {
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

//@Composable
//fun MyApp(navigateToDetails: (Product) -> Unit) {
//    val context = LocalContext.current
//    Scaffold(
//        topBar = {
////                 Snackbar()
//////            TopAppBar(title = { Text("Title") })
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    context.startActivity(DetailsActivity.newIntent(context, product = Product))
//                },
//                backgroundColor = Color.Red,
//                content = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_add),
//                        contentDescription = null,
//                        tint = Color.White
//                    )
//                }
//            )
//        },
//        content = {
//            ProductHomeContent(navigateToDetails = navigateToDetails)
//        }
//    )
//}




