package ru.rayanis.issuedproducts.data

data class UsedMaterial(
    val id: Int,
    val nameMaterial: Material,
    val quantityMaterial: Float = 0f,
    val unit: Material,
    val priceMaterial: Material,
    val costMaterial: Int = 0
)
