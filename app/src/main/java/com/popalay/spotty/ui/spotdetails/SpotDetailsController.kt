package com.popalay.spotty.ui.spotdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.popalay.spotty.R
import com.popalay.spotty.adapters.CommentsAdapter
import com.popalay.spotty.adapters.PhotosPagerAdapter
import com.popalay.spotty.models.Spot
import com.popalay.spotty.models.UiComment
import com.popalay.spotty.models.User
import com.popalay.spotty.ui.base.BaseController
import com.popalay.spotty.utils.extensions.inflate
import com.popalay.spotty.utils.extensions.toPx
import com.popalay.spotty.utils.extensions.trimInString
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.controller_spot_details.view.*


class SpotDetailsController() : SpotDetailsView, BaseController<SpotDetailsView, SpotDetailsPresenter>() {

    lateinit var spotId: String

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return container.inflate(R.layout.controller_spot_details, false)
    }

    constructor(spotId: String) : this() {
        this.spotId = spotId
    }

    override fun createPresenter() = SpotDetailsPresenter(spotId)

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
            sendComment.setOnClickListener {
                presenter.saveComment(commentText.text.trimInString())
            }
        }
    }

    override fun setBaseInfo(spot: Spot) {
        val adapter = PhotosPagerAdapter(spot.photoUrls)
        setMap(spot.position.toLatLng())
        with(view) {
            collapsingToolbar.title = spot.title
            collapsingToolbar.subtittle = spot.placeName
            photos_pager.adapter = adapter
            indicator.setViewPager(photos_pager)
            description.text = spot.description
        }
    }

    private fun setMap(latLng: LatLng) {
        val mapOptions = GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .liteMode(true)
                .camera(CameraPosition.fromLatLngZoom(latLng, 16f))
        val mapView = MapView(activity, mapOptions)
        with(mapView) {
            view.mapContainer.addView(this)
            layoutParams.height = 200.toPx()
            onCreate(args)
            getMapAsync { map ->
                map.uiSettings.isMapToolbarEnabled = false
                map.addMarker(MarkerOptions().position(latLng))
            }
        }
    }

    override fun setAuthor(user: User) {
        view.authorProfileImage.setImageURI(user.profilePhoto, activity)
        view.authorName.text = user.displayName
    }

    override fun onCommentSaved() {
        view.commentText.text = null
    }

    override fun showMessage(text: String) {
        showSnackbar(text)
    }

    override fun setComments(comments: List<UiComment>) {
        val commentsAdapter = CommentsAdapter()
        commentsAdapter.items = comments.toMutableList()
        view.commentsList.adapter = commentsAdapter
        view.commentsList.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity)
                .sizeResId(R.dimen.divider_size)
                .colorResId(R.color.divider)
                .marginResId(R.dimen.divider_margin, R.dimen.normal)
                .build())
    }
}
