package com.fadymarty.matule.di

import android.app.Application
import android.content.Context
import com.fadymarty.matule.data.repository.UserRepositoryImpl
import com.fadymarty.matule.domain.repository.UserRepository
import com.fadymarty.matule.domain.use_case.authentication.ValidateEmail
import com.fadymarty.matule.domain.use_case.authentication.ValidateName
import com.fadymarty.matule.domain.use_case.authentication.ValidatePassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        application: Application,
        authService: Auth,
        supabaseClient: SupabaseClient

    ): UserRepository {
        return UserRepositoryImpl(
            auth = authService,
            supabaseClient = supabaseClient,
            context = application
        )
    }

    @Provides
    @Singleton
    fun provideValidateNameUseCase(): ValidateName {
        return ValidateName()
    }

    @Provides
    @Singleton
    fun provideValidateEmailUseCase(): ValidateEmail {
        return ValidateEmail()
    }

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase(): ValidatePassword {
        return ValidatePassword()
    }
}