package com.popalay.spotty.di

import com.popalay.spotty.ui.addspot.AddSpotController
import com.popalay.spotty.ui.addspot.AddSpotPresenter
import com.popalay.spotty.ui.dashboard.DashboardPresenter
import com.popalay.spotty.ui.splash.SplashPresenter
import com.popalay.spotty.ui.spotdetails.SpotDetailsPresenter
import com.popalay.spotty.ui.spotsmap.SpotsMapPresenter
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
    fun inject(spotMapPresenter: SpotsMapPresenter)
}