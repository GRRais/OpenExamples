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
import androidx.compose.ui.res.stringResource
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
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Button(onClick = {
                navController.navigate("mainScreen")
            }) {
                Text("Добавить")
            }
            Button(onClick = {
                //TODO
            }) {
                Text("Рассчитать")
            }
        }
        Text("Карточка изделия ", textAlign = TextAlign.Center)
        var title = remember {
            DetailsProperty(stringResource(id = R.string.title))
        }
        DetailsProperty(stringResource(id = R.string.destination))
        DetailsProperty(stringResource(id = R.string.date))
        DetailsProperty(stringResource(id = R.string.quantity))
        DetailsProperty(stringResource(id = R.string.productCost))
        DetailsProperty(stringResource(id = R.string.description))
        DetailsProperty(stringResource(id = R.string.quantPersons))
    }
}

@Composable
private fun DetailsProperty( label: String) {
    var text by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.padding(6.dp)) {
        OutlinedTextField(
            value = text,
            label = {Text(text = label)},
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


