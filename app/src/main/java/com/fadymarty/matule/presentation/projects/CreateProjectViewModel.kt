package com.fadymarty.matule.presentation.projects

import androidx.lifecycle.ViewModel
import com.fadymarty.network.domain.use_case.projects.CreateProjectUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateProjectViewModel(
    private val createProjectUseCase: CreateProjectUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateProjectState())
    val state = _state.asStateFlow()
}