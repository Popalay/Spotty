package com.popalay.spotty.ui

import android.os.Bundle
import android.view.View
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.popalay.spotty.R
import com.popalay.spotty.ui.base.BaseActivity
import com.popalay.spotty.ui.splash.SplashController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(SplashController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun showProgressDialog() {
        loadingView.visibility = View.VISIBLE
    }

    override fun hideProgressDialog() {
        loadingView.visibility = View.GONE
    }
}