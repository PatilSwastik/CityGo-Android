package com.project.spass.presentation.screens.dashboard_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.spass.common.Resource
import com.project.spass.domain.use_case.get_pass.GetPassUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val passUseCase: GetPassUseCase
) : ViewModel() {
    private val _state = mutableStateOf<PassState>(PassState())
    val state: State<PassState> = _state

    init {
        getPass()
    }

    private fun getPass() {
        passUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = PassState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = PassState(pass = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value =
                        PassState(errorMessage = result.message ?: "Unexpected error.")
                }
            }
        }.launchIn(viewModelScope)
    }
}