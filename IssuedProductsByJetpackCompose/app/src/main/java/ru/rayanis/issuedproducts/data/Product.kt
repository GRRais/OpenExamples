package ru.rayanis.issuedproducts.data

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class Product(
    val id: Int,
    val title: String = "",
    val destination: String = "",
    val created: LocalDateTime,
    val quantity: Int = 0,
    val productCost: Int = 0,
    val description: String = "",
    val quantPersons: Int = 0,
    val list: MutableList<UsedMaterial>
): Serializable
