package com.popalay.spotty.di

import com.popalay.spotty.mvp.addspot.AddSpotController
import com.popalay.spotty.mvp.addspot.AddSpotPresenter
import com.popalay.spotty.mvp.dashboard.DashboardPresenter
import com.popalay.spotty.mvp.splash.SplashPresenter
import com.popalay.spotty.mvp.splash.SpotDetailsPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun plus(module: SessionModule): SessionComponent
    fun inject(splashPresenter: SplashPresenter)
    fun inject(dashboardPresenter: DashboardPresenter)
    fun inject(addSpotPresenter: AddSpotPresenter)
    fun inject(addSpotController: AddSpotController)
    fun inject(spotDetailsPresenter: SpotDetailsPresenter)
}