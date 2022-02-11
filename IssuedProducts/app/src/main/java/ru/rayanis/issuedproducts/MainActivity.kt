package ru.rayanis.issuedproducts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.rayanis.issuedproducts.data.DataProvider
import ru.rayanis.issuedproducts.data.Product
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
                        route = "details",
                    ) {
                       // val
                        DetailsScreen(this@MainActivity,
                            navController = navController
                        )
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
            .padding(end = 8.dp , bottom = 8.dp)
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
    activity: ComponentActivity,
    navController: NavController
) {

    var title by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var productCost by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantPerson by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                val product = Product(title, destination, date, quantity, productCost, description, quantPerson)
                DataProvider.saveProducts(activity, product)
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

        OutlinedTextField(
            value = title,
            label = { Text(text = stringResource(id = R.string.title)) },
            onValueChange = {
                title = it
            })
        OutlinedTextField(
            value = destination,
            label = { Text(text = stringResource(id = R.string.destination)) },
            onValueChange = {
                destination = it
            })
        OutlinedTextField(
            value = date,
            label = { Text(text = stringResource(id = R.string.date)) },
            onValueChange = {
                date = it
            })
        OutlinedTextField(
            value = quantity,
            label = { Text(text = stringResource(id = R.string.quantity)) },
            onValueChange = {
                quantity = it
            })
        OutlinedTextField(
            value = productCost,
            label = { Text(text = stringResource(id = R.string.productCost)) },
            onValueChange = {
                productCost = it
            })
        OutlinedTextField(
            value = description,
            label = { Text(text = stringResource(id = R.string.description)) },
            onValueChange = {
                description = it
            })
        OutlinedTextField(
            value = quantPerson,
            label = { Text(text = stringResource(id = R.string.quantPersons)) },
            onValueChange = {
                quantPerson = it
            })

        val message = remember{mutableStateOf("")}
        Column {
            Text(message.value, fontSize = 28.sp)
            TextField(
                value = message.value,
                textStyle = TextStyle(fontSize=25.sp) ,
                onValueChange = {newText -> message.value = newText}
            )
        }
    }
}

@Composable
fun DetailsProperty( label: String) {
//val product = Product()
    var title by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.padding(6.dp)) {

        OutlinedTextField(
            value = title,
            label = { Text(text = label) },
            onValueChange = {
                title = it
            })

    }
}


