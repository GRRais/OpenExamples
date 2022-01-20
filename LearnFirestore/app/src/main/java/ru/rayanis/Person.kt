package ru.rayanis

import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Person(
    var firstName: String,
    var lastName: String,
    var age: Int
)
