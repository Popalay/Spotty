package com.popalay.spotty.di

import com.popalay.spotty.mvp.addspot.AddSpotController
import com.popalay.spotty.mvp.dashboard.DashboardController
import com.popalay.spotty.mvp.dashboard.DashboardPresenter
import com.popalay.spotty.mvp.splash.SplashPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun plus(module: SessionModule): SessionComponent
    fun inject(splashController: DashboardController)
    fun inject(addSpotController: AddSpotController)
    fun inject(splashPresenter: SplashPresenter)
    fun inject(dashboardPresenter: DashboardPresenter)
}