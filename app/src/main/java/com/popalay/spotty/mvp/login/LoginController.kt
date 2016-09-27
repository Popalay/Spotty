package com.popalay.spotty.mvp.login

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.auth.ProviderName
import com.popalay.spotty.extensions.expand
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.extensions.toPx
import com.popalay.spotty.mvp.base.BaseController
import com.popalay.spotty.mvp.home.HomeController
import kotlinx.android.synthetic.main.controller_login.view.*
import javax.inject.Inject


class LoginController : LoginView, BaseController<LoginView, LoginPresenter>(), AuthManager.AuthListener {
    @Inject lateinit var authManager: AuthManager

    init {
        App.sessionComponent.inject(this)
        authManager.addAuthListener(this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_login, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        initUI(view)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        expandButtonsBox()
    }

    override fun createPresenter() = LoginPresenter()

    override fun onActivityResumed(activity: Activity?) {
        super.onActivityResumed(activity)
        hideProgress()
    }

    private fun initUI(view: View) {
        view.btn_google.setOnClickListener {
            showProgress()
            authManager.signIn(ProviderName.GOOGLE, this)
        }
        view.btn_vk.setOnClickListener {
            showProgress()
            authManager.signIn(ProviderName.VK, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authManager.handleSignIn(requestCode, resultCode, data)
    }

    override fun onDetach(view: View) {
        authManager.removeAuthListener(this)
        super.onDetach(view)
    }

    private fun expandButtonsBox() {
        view.buttons_box.expand(220.toPx())
    }

    override fun authCompleted() {
        hideProgress()
        toHome()
    }

    override fun authFailed() {
        hideProgress()
        showSnackbar("Authentication failed")
    }

    private fun toHome() {
        router.setRoot(RouterTransaction.with(HomeController())
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }

}
