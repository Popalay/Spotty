package com.popalay.spotty.mvp.login

import com.hannesdorfmann.mosby.mvp.MvpView


interface LoginView : MvpView {
    fun showProgress()
    fun hideProgress()
}