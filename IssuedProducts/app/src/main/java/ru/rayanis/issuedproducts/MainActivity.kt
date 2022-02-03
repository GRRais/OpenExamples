package ru.rayanis.issuedproducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.rayanis.issuedproducts.ui.theme.IssuedProductsTheme

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IssuedProductsTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "mainScreen"
                ) {
                    composable("mainScreen") {
                        LoginScreen(navController)
                    }
                    composable(
                        route = "details"
                    ) {
                        DetailsScreen(
                            navController = navController
                        )
                    }
                    composable("post/{showOnlyPostsByUser}", arguments = listOf(
                        navArgument("showOnlyPostsByUser") {
                            type = NavType.BoolType
                            defaultValue = false
                        }
                    )) {
                        val showOnlyPostsByUser =
                            it.arguments?.getBoolean("showOnlyPostsByUser") ?: false
                        PostScreen(showOnlyPostsByUser)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
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
            navController.navigate("details")
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
fun DetailsScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Details Screen: ", textAlign = TextAlign.Center)
        DetailsProperty()
        DetailsProperty()
        Button(onClick = {
            navController.navigate("post/true")
        }) {
            Text("Go to Post Screen")
        }
    }
}

@Composable
private fun DetailsProperty( value: String = "") {
    var text by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = text,
            label = {
                Text(text = "label")},
            onValueChange = {
                text = it
            })
    }
}

@Composable
fun PostScreen(
    showOnlyPostsByUser: Boolean = false
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Post Screen, $showOnlyPostsByUser")
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


