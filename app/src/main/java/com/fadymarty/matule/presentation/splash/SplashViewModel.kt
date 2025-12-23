package com.fadymarty.matule.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.network.domain.use_case.user.GetPinUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class SplashViewModel(
    private val getPinUseCase: GetPinUseCase,
) : ViewModel() {
    
    private val eventChannel = Channel<SplashEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        getPinUseCase().onEach { pin ->
            delay(1500)
            if (pin != null) {
                eventChannel.send(SplashEvent.NavigateToEnterPin)
            } else {
                eventChannel.send(SplashEvent.NavigateToLogin)
            }
        }.launchIn(viewModelScope)
    }
}