package com.popalay.spotty.mvp.splash

import com.hannesdorfmann.mosby.mvp.MvpView


interface SplashView : MvpView {

    fun userIsLogged()
    fun userNotLogged()
}