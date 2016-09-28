package com.popalay.spotty.di

import com.popalay.spotty.mvp.home.HomeController
import com.popalay.spotty.mvp.login.LoginController
import com.popalay.spotty.mvp.login.LoginPresenter
import dagger.Subcomponent

@SessionScope
@Subcomponent(modules = arrayOf(SessionModule::class))
interface SessionComponent {

    fun inject(loginController: LoginController)

    fun inject(loginController: HomeController)

    fun inject(loginPresenter: LoginPresenter)
}