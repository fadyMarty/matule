package com.fadymarty.matule.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fadymarty.matule.common.util.Constants
import com.fadymarty.matule.common.util.Constants.USER_SETTINGS
import com.fadymarty.matule.data.remote.dto.UserDto
import com.fadymarty.matule.domain.repository.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(USER_SETTINGS)

class UserRepositoryImpl @Inject constructor(
    private val context: Context,
    private val auth: Auth,
    private val supabaseClient: SupabaseClient
) : UserRepository {

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey(Constants.ACCESS_TOKEN)
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Boolean {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val userDto = UserDto(
                id = UUID.randomUUID().toString(),
                name = name,
                email = email
            )
            supabaseClient.from("users")
                .upsert(userDto)
            saveToken()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            saveToken()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return try {
            val token = getToken().firstOrNull()
            if (token.isNullOrEmpty()) {
                false
            } else {
                auth.retrieveUser(token)
                auth.refreshCurrentSession()
                saveToken()
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun saveToken() {
        val accessToken = auth.currentAccessTokenOrNull()
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken ?: ""
        }
    }

    private fun getToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] ?: ""
        }
    }
}