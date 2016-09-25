package com.popalay.spotty.di

import android.content.Context
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.auth.providers.AuthProvider
import com.popalay.spotty.auth.AuthProviderFactory
import dagger.Module
import dagger.Provides

@Module
class SessionModule() {

    @Provides
    @SessionScope
    fun provideAuthProviderFactory(context: Context): AuthProviderFactory {
        return AuthProviderFactory(context)
    }

    @Provides
    @SessionScope
    fun provideAuthManager(authProviderFactory: AuthProviderFactory): AuthManager {
        return AuthManager(authProviderFactory)
    }
}

