package com.popalay.spotty.ui.spotdetails

import android.support.v7.widget.DividerItemDecoration
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
import com.popalay.spotty.models.CommentUI
import com.popalay.spotty.models.FullSpotDetails
import com.popalay.spotty.ui.base.BaseController
import com.popalay.spotty.utils.extensions.inflate
import com.popalay.spotty.utils.extensions.toPx
import kotlinx.android.synthetic.main.controller_spot_details.view.*
import kotlinx.android.synthetic.main.header_comments.view.*

class SpotDetailsController() : SpotDetailsView, BaseController<SpotDetailsView, SpotDetailsPresenter>() {

    lateinit var spotId: String
    lateinit var mCommentsAdapter: CommentsAdapter

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
            /*          sendComment.setOnClickListener {
                          presenter.saveComment(commentText.text.trimInString())
                      }*/

            commentsList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        }
    }

    override fun setBaseInfo(fullSpotDetails: FullSpotDetails) {
        val photosPagerAdapter = PhotosPagerAdapter(fullSpotDetails.spot.photoUrls)
        with(view) {
            collapsingToolbar.title = fullSpotDetails.spot.title
            photos_pager.adapter = photosPagerAdapter
            indicator.setViewPager(photos_pager)
            description.text = fullSpotDetails.spot.description
            authorProfileImage.setImageURI(fullSpotDetails.author.profilePhoto, context)
            authorName.text = fullSpotDetails.author.displayName
            setMap(fullSpotDetails.spot.position.toLatLng(), this)
        }
    }

    private fun setMap(latLng: LatLng, view: View) {
        val mapOptions = GoogleMapOptions()
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
                .liteMode(true)
                .camera(CameraPosition.fromLatLngZoom(latLng, 16f))
        val mapView = MapView(view.context, mapOptions)
        with(mapView) {
            view.mapContainer.addView(this)
            layoutParams.height = 200.toPx()
            onCreate(null)
            getMapAsync { map ->
                map.uiSettings.isMapToolbarEnabled = false
                map.addMarker(MarkerOptions().position(latLng))
            }
        }
    }

    override fun onCommentSaved() {
        view.commentText.text = null
    }

    override fun showMessage(text: String) {
        showSnackbar(text)
    }

    override fun setComments(comments: List<CommentUI>) {
        mCommentsAdapter = CommentsAdapter()
        mCommentsAdapter.items = comments.toMutableList()
        view.commentsList.adapter = mCommentsAdapter
    }
}
