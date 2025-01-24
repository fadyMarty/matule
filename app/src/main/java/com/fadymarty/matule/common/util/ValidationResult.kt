package com.fadymarty.matule.common.util

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)