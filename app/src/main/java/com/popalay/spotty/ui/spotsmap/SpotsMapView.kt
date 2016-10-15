package com.popalay.spotty.ui.spotsmap

import com.google.android.gms.maps.model.LatLng
import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.models.Spot


interface SpotsMapView : MvpView {
    fun showMarker(spot: Spot)
    fun setLocation(position: LatLng)
}