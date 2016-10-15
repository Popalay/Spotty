package com.popalay.spotty.di

import com.popalay.spotty.ui.home.HomePresenter
import com.popalay.spotty.ui.login.LoginPresenter
import dagger.Subcomponent

@SessionScope
@Subcomponent(modules = arrayOf(SessionModule::class))
interface SessionComponent {
    fun inject(loginPresenter: LoginPresenter)
    fun inject(homePresenter: HomePresenter)
}