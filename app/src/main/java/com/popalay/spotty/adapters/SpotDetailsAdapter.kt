package com.popalay.spotty.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.popalay.spotty.R
import com.popalay.spotty.models.CommentUI
import com.popalay.spotty.models.FullSpotDetails
import com.popalay.spotty.utils.extensions.inflate
import com.popalay.spotty.utils.extensions.toPx
import com.popalay.spotty.utils.ui.StickyHeaderAdapter
import kotlinx.android.synthetic.main.content_spot_details.view.*
import kotlinx.android.synthetic.main.item_comment.view.*
import java.util.*

class SpotDetailsAdapter(val fullSpotDetails: FullSpotDetails) : UltimateAdapter<ViewHolder>(),
        UltimateAdapter.HeaderInterface, StickyHeaderAdapter {

    var items: MutableList<CommentUI> = ArrayList()

    override fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getDataSize() = items.size

    override fun getDataViewResId(viewType: Int) = R.layout.item_comment

    override fun getDataId(dataPosition: Int) = dataPosition.toLong()

    override fun getDataViewType(dataPosition: Int) = 1

    override fun getDataViewHolder(v: View, dataViewType: Int) = ViewHolder(v)

    override fun bindDataVH(vh: ViewHolder, dataPosition: Int) {
        val item = items[dataPosition]
        with(vh.itemView) {
            comment.text = item.text
            commentAuthorProfileImage.setImageURI(item.authorPhotoUrl)
        }
    }

    override fun getHeaderVH(v: View): HeaderVH {
        return HeaderViewHolder(v)
    }

    override fun getHeaderViewResId() = R.layout.content_spot_details

    override fun bindHeaderVH(vh: RecyclerView.ViewHolder) {
        with(vh.itemView) {
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

    override fun getHeaderId(position: Int): Long {
        return 1
    }

    override fun createStickyHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(R.layout.header_comments)
        return HeaderViewHolder(view)
    }

    override fun bindStickyHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }
}
