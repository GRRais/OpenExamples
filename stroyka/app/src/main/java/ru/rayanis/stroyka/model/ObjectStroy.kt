package ru.rayanis.stroyka.model

import java.io.Serializable

data class ObjectStroy (
    val area: String? = null,
    val village: String? = null,
    val organization: String? = null,
    val description: String? = null,
    val key: String? = null,
    val uid: String? = null
): Serializable