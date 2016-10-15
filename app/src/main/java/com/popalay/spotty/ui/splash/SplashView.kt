package com.popalay.spotty.ui.splash

import com.hannesdorfmann.mosby.mvp.MvpView


interface SplashView : MvpView {

    fun userIsLogged()
    fun userNotLogged()
}