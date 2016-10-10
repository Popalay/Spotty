package com.popalay.spotty.mvp.dashboard

import com.google.android.gms.maps.model.LatLng
import com.hannesdorfmann.mosby.mvp.MvpView
import com.popalay.spotty.models.Spot


interface DashboardView : MvpView {
    fun setData(data: List<Spot>)
    fun showMarker(spot: Spot)
    fun setLocation(position: LatLng)
    fun startAddSpot()
    fun openSpot(spot: Spot)
}