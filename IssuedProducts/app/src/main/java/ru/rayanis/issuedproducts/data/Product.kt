package ru.rayanis.issuedproducts.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Product(
    var title: String = "" ,
    val destination: String = "" ,
    val date: String ,
    val quantity: String ,
    val productCost: String ,
    val description: String = "" ,
    val quantPersons: String ,
//    val list: MutableList<UsedMaterial>
):Parcelable
