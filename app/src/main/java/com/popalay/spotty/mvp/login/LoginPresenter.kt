package com.popalay.spotty.mvp.login

import com.bluelinelabs.conductor.Controller
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import com.popalay.spotty.App
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.auth.ProviderName
import javax.inject.Inject


class LoginPresenter : MvpBasePresenter<LoginView>(), AuthManager.AuthListener {

    @Inject lateinit var authManager: AuthManager

    init {
        App.sessionComponent.inject(this)
    }

    override fun attachView(view: LoginView?) {
        authManager.addAuthListener(this)
    }

    override fun detachView(retainInstance: Boolean) {
        authManager.removeAuthListener(this)
    }

    fun signIn(providerName: ProviderName, controller: Controller) {
        view?.showProgress()
        authManager.signIn(providerName, controller)
    }

    override fun authCompleted() {

    }

    override fun authFailed() {

    }

}