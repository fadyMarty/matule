package com.fadymarty.matule.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signUp(name: String, email: String, password: String): Boolean

    suspend fun signIn(email: String, password: String): Boolean

    suspend fun isUserLoggedIn(): Boolean
}