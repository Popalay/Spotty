package com.popalay.spotty.ui.dashboard

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView
import com.popalay.spotty.models.Spot


interface DashboardView : MvpLceView<List<Spot>> {
    fun startAddSpot()
    fun openSpot(spot: Spot)
}