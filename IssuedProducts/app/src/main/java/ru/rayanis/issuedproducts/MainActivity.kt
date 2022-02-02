package ru.rayanis.issuedproducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.rayanis.issuedproducts.ui.theme.IssuedProductsTheme

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IssuedProductsTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController ,
                    startDestination = "productsScreen"
                ) {
                    composable("productsScreen") {
                        ProductsScreen(navController)
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
            .padding(end = 8.dp, bottom = 8.dp)
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
fun ProfileScreen(
    navController:NavController,

) {

}

