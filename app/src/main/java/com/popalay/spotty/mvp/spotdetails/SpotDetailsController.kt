package com.popalay.spotty.mvp.spotdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.R
import com.popalay.spotty.adapters.PhotosPagerAdapter
import com.popalay.spotty.extensions.inflate
import com.popalay.spotty.models.Spot
import com.popalay.spotty.mvp.base.BaseController
import com.popalay.spotty.mvp.splash.SpotDetailsPresenter
import kotlinx.android.synthetic.main.controller_spot_details.view.*


class SpotDetailsController() : SpotDetailsView, BaseController<SpotDetailsView, SpotDetailsPresenter>() {

    lateinit var spot: Spot

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_spot_details, false)
    }

    constructor(spot: Spot) : this() {
        this.spot = spot
    }

    override fun createPresenter() = SpotDetailsPresenter()

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        initUI(view)
    }

    private fun initUI(view: View) {
        setSupportActionBar(view.toolbar)
        setTitle(spot.title)
        getSupportActionBar()?.setHomeButtonEnabled(true)
        with(view) {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                router.popCurrentController()
            }
            val adapter = PhotosPagerAdapter(spot.photoUrls)
            photos_pager.adapter = adapter
            indicator.setViewPager(photos_pager)
        }
    }

}