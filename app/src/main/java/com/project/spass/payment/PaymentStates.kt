package com.project.spass.payment


data class PaymentStates(
    val success: Boolean = false,
    val cancel: Boolean = false,
    val error: Boolean = false,
    val errorMessage: String? = ""
)