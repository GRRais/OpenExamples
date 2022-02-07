package ru.rayanis.issuedproducts.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Material(
    val name: String = "",
    val unit: String = "",
    val price: Float = 0f
):Parcelable
