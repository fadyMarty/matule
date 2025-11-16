package com.fadymarty.matule.presentation.projects

data class CreateProjectState(
    val isLoading: Boolean = false,
    val type: String? = null,
    val title: String = "",
    val dateStart: String = "",
    val dateEnd: String = "",
    val userId: String? = null,
    val descriptionSource: String = "",
    val category: String? = null,
    val image: String? = null,
)