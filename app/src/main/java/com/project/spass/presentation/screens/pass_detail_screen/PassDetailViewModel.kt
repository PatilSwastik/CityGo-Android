package com.project.spass.presentation.screens.pass_detail_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.spass.common.Constrains
import com.project.spass.common.Resource
import com.project.spass.domain.use_case.get_pass_detail.GetPassDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PassDetailViewModel @Inject constructor(
    private val getPassDetailUseCase: GetPassDetailUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    //state
    private val _state = mutableStateOf(PassDetailState())
    val state: State<PassDetailState> = _state

    init {
        val passId = stateHandle.get<String>(Constrains.PASS_ID_PARAM)
        getPassDetail(passId!!.toInt())
    }

    private fun getPassDetail(passId: Int) {
        getPassDetailUseCase(passId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = PassDetailState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = PassDetailState(passDetail = result.data)
                }
                is Resource.Error -> {
                    _state.value = PassDetailState(errorMessage = result.message!!)
                }
            }

        }.launchIn(viewModelScope)
    }
}