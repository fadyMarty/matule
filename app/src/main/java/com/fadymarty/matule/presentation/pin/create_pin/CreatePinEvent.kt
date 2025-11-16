package com.fadymarty.matule.presentation.pin.create_pin

sealed class CreatePinEvent {
    data class SavePin(val pin: String) : CreatePinEvent()
}