package com.popalay.spotty.ui.login

import com.hannesdorfmann.mosby.mvp.MvpView


interface LoginView : MvpView {
    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun loginSuccessful()
}