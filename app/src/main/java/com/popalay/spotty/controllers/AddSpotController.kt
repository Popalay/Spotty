package com.popalay.spotty.controllers

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.extensions.inflate


class AddSpotController : BaseController() {

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_add_spot, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        (parentController as HomeController).showArrow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                (parentController as HomeController).hideArrow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun handleBack(): Boolean {
        (parentController as HomeController).hideArrow()
        return super.handleBack()
    }
}