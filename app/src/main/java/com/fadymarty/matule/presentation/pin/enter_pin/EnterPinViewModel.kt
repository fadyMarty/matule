package com.fadymarty.matule.presentation.pin.enter_pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.use_case.user.GetPinUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class EnterPinViewModel(
    private val getPinUseCase: GetPinUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EnterPinState())
    val state = _state.asStateFlow()

    init {
        getPinUseCase().onEach { pin ->
            _state.update { it.copy(pin = pin) }
        }.launchIn(viewModelScope)
    }
}