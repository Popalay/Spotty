package com.popalay.spotty.ui.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.popalay.spotty.R
import com.popalay.spotty.ui.base.BaseController
import com.popalay.spotty.ui.home.HomeController
import com.popalay.spotty.ui.login.LoginController

class SplashController : SplashView, BaseController<SplashView, SplashPresenter>() {

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_splash, container, false)
    }

    override fun userIsLogged() {
        router.replaceTopController(RouterTransaction.with(HomeController())
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler()))
    }

    override fun userNotLogged() {
        router.replaceTopController(RouterTransaction.with(LoginController()))
    }

    override fun createPresenter() = SplashPresenter()

}
