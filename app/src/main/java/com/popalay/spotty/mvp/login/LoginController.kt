package com.popalay.spotty.mvp.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.popalay.spotty.R
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.auth.ProviderName
import com.popalay.spotty.extensions.expand
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.extensions.toPx
import com.popalay.spotty.mvp.base.BaseController
import com.popalay.spotty.mvp.home.HomeController
import kotlinx.android.synthetic.main.controller_login.view.*


class LoginController : LoginView, BaseController<LoginView, LoginPresenter>(), AuthManager.AuthListener {

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

    private fun initUI(view: View) {
        view.btn_google.setOnClickListener {
            presenter.signIn(ProviderName.GOOGLE, this)
        }
        view.btn_vk.setOnClickListener {
            presenter.signIn(ProviderName.VK, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //authManager.handleSignIn(requestCode, resultCode, data)
    }

    private fun expandButtonsBox() {
        view.buttons_box.expand(220.toPx())
    }

    override fun authCompleted() {
        hideProgressDialog()
        toHome()
    }

    override fun authFailed() {
        hideProgressDialog()
        showSnackbar("Authentication failed")
    }

    private fun toHome() {
        router.setRoot(RouterTransaction.with(HomeController())
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }

    override fun showProgress() {
        showProgressDialog()
    }

    override fun hideProgress() {
        hideProgressDialog()
    }
}
