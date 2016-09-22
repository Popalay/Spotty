package com.popalay.spotty.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.google.firebase.auth.FirebaseAuth
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.auth.AuthManager
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.extensions.inflate
import kotlinx.android.synthetic.main.controller_home.view.*
import javax.inject.Inject

class HomeController : BaseController() {

    @Inject lateinit var authManager: dagger.Lazy<AuthManager>

    init {
        App.sessionComponent.inject(this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_home)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        initUI()
    }

    private fun initUI() {
        setSupportActionBar(view.toolbar)
        FirebaseAuth.getInstance().currentUser?.let {
            view.display_name.text = it.displayName
            view.profile_photo.text = it.photoUrl?.path
        }
        view.btn_exit.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        authManager.get().signOut()
        router.replaceTopController(RouterTransaction.with(LoginController())
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler()))
    }

}
