package com.popalay.spotty.ui.dashboard

import com.google.android.gms.maps.model.LatLng
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView
import com.popalay.spotty.models.Spot


interface DashboardView : MvpLceView<List<Spot>> {
    fun showMarker(spot: Spot)
    fun setLocation(position: LatLng)
    fun startAddSpot()
    fun openSpot(spot: Spot)
}