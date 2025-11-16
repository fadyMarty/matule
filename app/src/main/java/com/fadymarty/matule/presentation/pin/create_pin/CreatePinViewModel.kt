package com.fadymarty.matule.presentation.pin.create_pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.use_case.user.SavePinUseCase
import kotlinx.coroutines.launch

class CreatePinViewModel(
    private val savePinUseCase: SavePinUseCase,
) : ViewModel() {

    fun onEvent(event: CreatePinEvent) {
        when (event) {
            is CreatePinEvent.SavePin -> {
                viewModelScope.launch {
                    savePinUseCase(event.pin)
                }
            }
        }
    }
}