package ru.rayanis.issuedproducts.data

data class UsedMaterial(
    val id: Int,
    val nameMaterial: Material,
    val quantityMaterial: Float,
    val unit: Material,
    val priceMaterial: Material,
    val costMaterial: Int
)
