package com.project.spass.presentation.screens.dashboard_screen

import com.project.spass.domain.model.PassModel

data class PassState(
    val isLoading: Boolean = false,
    val pass: List<PassModel>? = null,
    val errorMessage: String = ""
)
