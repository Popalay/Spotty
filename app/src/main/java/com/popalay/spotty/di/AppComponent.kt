package com.popalay.spotty.di

import com.popalay.spotty.controllers.DashboardController
import com.popalay.spotty.controllers.SplashController
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun plus(module: SessionModule): SessionComponent
    fun inject(splashController: SplashController)
    fun inject(splashController: DashboardController)
}