package com.popalay.spotty.controllers

import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.App
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.controllers.base.DrawerController
import com.popalay.spotty.extensions.inflate
import kotlinx.android.synthetic.main.controller_home.view.*

class HomeController : DrawerController() {

    init {
        App.sessionComponent.inject(this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = container.inflate(R.layout.controller_home, false)
        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        initUI()
    }

    override fun getContainer(): ViewGroup = view.home_container

    override fun provideToolbar(): Toolbar = view.toolbar

    private fun initUI() {
        setTitle(activity.getString(R.string.app_name))
    }

    override fun getControllerByPosition(position: Long): BaseController {
        return when (position.toInt()) {
            -1 -> DashboardController()
            else -> DashboardController()
        }
    }

    override fun handleBack(): Boolean {
        return super.handleBack()
    }

}
