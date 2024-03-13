package com.project.spass.presentation.screens.pass_detail_screen

import com.project.spass.domain.model.PassModel

data class PassDetailState(
    val isLoading: Boolean = false,
    val passDetail: PassModel? = null,
    val errorMessage: String = ""
)