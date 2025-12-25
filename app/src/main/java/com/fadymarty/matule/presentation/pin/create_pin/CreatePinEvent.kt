package com.fadymarty.matule.presentation.pin.create_pin

sealed interface CreatePinEvent {
    data class SavePin(val pin: String) : CreatePinEvent
}