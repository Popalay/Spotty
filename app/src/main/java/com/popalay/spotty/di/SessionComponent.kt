package com.popalay.spotty.di

import com.popalay.spotty.controllers.HomeController
import com.popalay.spotty.controllers.LoginController
import com.popalay.spotty.controllers.base.DrawerController
import dagger.Subcomponent

@SessionScope
@Subcomponent(modules = arrayOf(SessionModule::class))
interface SessionComponent {

    fun inject(loginController: LoginController)

    fun inject(loginController: HomeController)
    fun inject(drawerController: DrawerController)
}