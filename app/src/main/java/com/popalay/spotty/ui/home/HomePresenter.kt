package com.popalay.spotty.ui.home

import com.popalay.spotty.App
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.data.DataManager
import com.popalay.spotty.models.User
import com.popalay.spotty.ui.base.presenter.RxPresenter
import dagger.Lazy
import javax.inject.Inject


class HomePresenter : RxPresenter<HomeView>() {

    @Inject lateinit var authManager: Lazy<AuthManager>
    @Inject lateinit var dataManager: DataManager

    init {
        App.sessionComponent.inject(this)
    }

    override fun attachView(view: HomeView) {
        super.attachView(view)
        setUserInfo()
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
    }

    private fun setUserInfo() {
        dataManager.getCurrentUser()?.let {
            view?.setUserInfo(User(displayName = it.displayName, profilePhoto = it.photoUrl))
        }
    }

    fun signOut() {
        view?.startSignIn()
        authManager.get().signOut()
    }

    fun open(position: Int) {

    }
}