package ru.rayanis.issuedproducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ru.rayanis.issuedproducts.ui.theme.IssuedProductsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IssuedProductsTheme {
                Scaffold(
                    topBar = {
//                 Snackbar()
////            TopAppBar(title = { Text("Title") })
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {

                            },
                            backgroundColor = Color.Red,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        )
                    },
                    content = {
                        ProductHomeContent(navigateToDetails = navigateToDetails)
                    }
            }
        }
    }
}

@Composable
fun ProductsScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
//                 Snackbar()
////            TopAppBar(title = { Text("Title") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                },
                backgroundColor = Color.Red,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )
        },
        content = {
            navController.navigate()
        }
    )
}