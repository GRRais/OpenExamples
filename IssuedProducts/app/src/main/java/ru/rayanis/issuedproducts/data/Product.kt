package ru.rayanis.issuedproducts.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Product(
    val id: Int,
    val title: String = "",
    val destination: String = "",
    val date: String,
    val quantity: Int = 0,
    val productCost: Int = 0,
    val description: String = "",
    val quantPersons: Int = 0,
    val list: MutableList<UsedMaterial>
):Parcelable
