package ru.rayanis.issuedproducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rayanis.issuedproducts.data.DataProvider
import ru.rayanis.issuedproducts.data.Product
import ru.rayanis.issuedproducts.ui.theme.IssuedProductsTheme
import java.lang.Exception

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
                        route = "details",
                        )
                    {
                        val product: Product
                        DetailsScreen(
                            navController = navController,
                            title = product.title.toString(),
                            userId = userId,
                            created = timestamp
                        )
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




