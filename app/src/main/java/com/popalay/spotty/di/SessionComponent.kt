package com.popalay.spotty.di

import com.popalay.spotty.mvp.home.HomePresenter
import com.popalay.spotty.mvp.login.LoginPresenter
import dagger.Subcomponent

@SessionScope
@Subcomponent(modules = arrayOf(SessionModule::class))
interface SessionComponent {
    fun inject(loginPresenter: LoginPresenter)
    fun inject(homePresenter: HomePresenter)
}