package ru.rayanis.stroyka.model

import java.io.Serializable

data class ObjectStroy (
    val area: String? = null,
    val village: String? = null,
    val organization: String? = null,
    val description: String? = null,
    val mainImage: String? = null,
    val image2: String? = null,
    val image3: String? = null,
    val key: String? = null,
    val uid: String? = null,
    var isActive: Boolean = false
): Serializable