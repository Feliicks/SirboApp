package com.felicksdev.onlymap.data.models.error

data class ErrorDetails(
    val id: Int,
    val message: String,
    val msg: String,
    val noPath: Boolean
)