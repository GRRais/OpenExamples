package ru.rayanis.issuedproducts.data

data class Material(
    val id: Int,
    val name: String = "",
    val unit: String = "",
    val price: Float = 0f
)
