package com.fadymarty.matule.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadymarty.matule.domain.repository.UserRepository
import com.fadymarty.matule.presentation.navigation.graph.Graph
import com.fadymarty.matule.presentation.navigation.screen.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    private val _startDestination = MutableStateFlow(Screen.Onboarding1.route)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            if (userRepository.isUserLoggedIn()) {
                _startDestination.value = Graph.MAIN
            } else {
                _startDestination.value = Screen.Onboarding1.route
            }
        }
    }
}