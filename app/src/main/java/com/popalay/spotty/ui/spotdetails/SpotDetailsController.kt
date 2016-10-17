package com.popalay.spotty.ui.spotdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.R
import com.popalay.spotty.adapters.PhotosPagerAdapter
import com.popalay.spotty.models.Spot
import com.popalay.spotty.models.User
import com.popalay.spotty.ui.base.BaseController
import com.popalay.spotty.ui.splash.SpotDetailsPresenter
import com.popalay.spotty.utils.extensions.inflate
import kotlinx.android.synthetic.main.controller_spot_details.view.*


class SpotDetailsController() : SpotDetailsView, BaseController<SpotDetailsView, SpotDetailsPresenter>() {

    lateinit var spot: Spot

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_spot_details, false)
    }

    constructor(spot: Spot) : this() {
        this.spot = spot
    }

    override fun createPresenter() = SpotDetailsPresenter(spot)

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        initUI(view)
    }

    private fun initUI(view: View) {
        setSupportActionBar(view.toolbar)
        getSupportActionBar()?.setHomeButtonEnabled(true)
        with(view) {
            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                router.popCurrentController()
            }
        }
    }

    override fun setBaseInfo(spot: Spot) {
        setTitle(spot.title)
        val adapter = PhotosPagerAdapter(spot.photoUrls)
        with(view) {
            photos_pager.adapter = adapter
            indicator.setViewPager(photos_pager)
        }
    }

    override fun setAuthor(user: User) {
        view.authorProfileImage.setImageURI(user.profilePhoto, activity)
        view.authorName.text = user.displayName
    }
}
