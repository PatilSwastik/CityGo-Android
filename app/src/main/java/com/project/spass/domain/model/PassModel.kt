package com.project.spass.domain.model

data class PassModel(
    val id: Int,
    val source: String,
    val destination: String,
    val price: Double,
)