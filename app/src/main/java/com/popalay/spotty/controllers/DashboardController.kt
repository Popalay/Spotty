package com.popalay.spotty.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.extensions.inflate


class DashboardController : BaseController() {


    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_dashboard)
    }


}