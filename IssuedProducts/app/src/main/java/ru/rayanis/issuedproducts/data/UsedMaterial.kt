package ru.rayanis.issuedproducts.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UsedMaterial(
    val nameMaterial: Material,
    val quantityMaterial: Float = 0f,
    val unit: Material,
    val priceMaterial: Material,
    val costMaterial: Int = 0
): Parcelable
