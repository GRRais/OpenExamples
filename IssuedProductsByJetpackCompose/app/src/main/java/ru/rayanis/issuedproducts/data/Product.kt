package ru.rayanis.issuedproducts.data

import java.io.Serializable
import java.util.*

data class Product(
    val id: Int,
    val title: String = "",
    val destination: String = "",
    val date: Date,
    val quantity: Int = 0,
    val productCost: Int = 0,
    val description: String = "",
    val quantPersons: Int = 0,
    val list: MutableList<UsedMaterial>
): Serializable
