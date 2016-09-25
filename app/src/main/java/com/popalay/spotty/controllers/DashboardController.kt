package com.popalay.spotty.controllers

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlacePicker
import com.pawegio.kandroid.d
import com.popalay.spotty.R
import com.popalay.spotty.controllers.base.BaseController
import com.popalay.spotty.extensions.inflate
import kotlinx.android.synthetic.main.controller_dashboard.view.*


class DashboardController : BaseController() {


    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_dashboard, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        d("sss")
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        initUI()
    }

    override fun onActivityStarted(activity: Activity?) {
        super.onActivityStarted(activity)
        d("started")
    }

    private fun initUI() {
        d("initUI")
        setTitle("hhh")
        view.pick.setOnClickListener {
            d("gghg")
            // Construct an intent for the place picker
            try {
                val intentBuilder = PlacePicker.IntentBuilder()
                val intent = intentBuilder.build(activity)
                // Start the intent by requesting a result,
                // identified by a request code.
                startActivityForResult(intent, 101)
                d("hgh")

            } catch (e: GooglePlayServicesRepairableException) {
                // ...
            } catch (e: GooglePlayServicesNotAvailableException) {
                // ...
            }

        }
    }

}