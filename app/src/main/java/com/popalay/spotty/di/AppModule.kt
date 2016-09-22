package com.popalay.spotty.di

import android.content.Context
import com.popalay.spotty.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app;
    }

    @Provides
    @Singleton
    fun provideApplication(): App {
        return app;
    }
}

