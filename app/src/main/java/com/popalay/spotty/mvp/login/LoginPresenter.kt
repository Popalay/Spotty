package com.popalay.spotty.mvp.login

import android.content.Intent
import com.bluelinelabs.conductor.Controller
import com.popalay.spotty.App
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.auth.ProviderName
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.User
import com.popalay.spotty.mvp.base.presenter.RxPresenter
import javax.inject.Inject


class LoginPresenter : RxPresenter<LoginView>(), AuthManager.AuthListener {

    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var dataManager: DataManager

    init {
        App.sessionComponent.inject(this)
    }

    override fun attachView(view: LoginView) {
        super.attachView(view)
        authManager.addAuthListener(this)
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        authManager.removeAuthListener(this)
    }

    fun handleSignIn(requestCode: Int, resultCode: Int, data: Intent?) {
        authManager.handleSignIn(requestCode, resultCode, data)
    }

    fun signIn(providerName: ProviderName, controller: Controller) {
        view?.showProgress()
        authManager.signIn(providerName, controller)
    }

    override fun authCompleted(user: User) {
        //dataManager.saveUser(user)
        view?.hideProgress()
        view?.loginSuccessful()
    }

    override fun authFailed() {
        view?.hideProgress()
        view?.showError("Authentication failed")
    }

}