package ru.rayanis.issuedproducts.data

import java.io.Serializable
import java.util.*

data class Product(
    val id: Int,
    val title: String,
    val destination: String,
    val date: Date,
    val quantity: Int,
    val productCost: Int,
    val description: String,
    val quantPersons: Int,
    val list: MutableList<UsedMaterial>
): Serializable
